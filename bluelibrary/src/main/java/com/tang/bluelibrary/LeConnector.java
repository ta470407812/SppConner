package com.tang.bluelibrary;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.os.Build;

import com.tang.bluelibrary.callback.ConnectCallback;
import com.tang.bluelibrary.callback.LeConnectCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public class LeConnector {

    private final String TAG = getClass().getSimpleName();

    public static final int LE_SUCCESS = 0;
    public static final int LE_ERROR = 1;

    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_DISCONNECTED = 0;

    private BluetoothGatt mBluetoothGatt;
    private BluetoothGattCharacteristic mCharacteristicTx;

    private List<ConnectCallback> mConnectCallbacks;

    private Object mStateLock = new Object();
    private Object mCallbackLock = new Object();
    private int mConnState = STATE_DISCONNECTED;

    private UUID mDescriptor;

    public LeConnector() {
        mConnectCallbacks = new ArrayList<>();
    }

    public void addConnectCallback(ConnectCallback connectCallback) {
        synchronized (mCallbackLock) {
            if (!mConnectCallbacks.contains(connectCallback)) {
                mConnectCallbacks.add(connectCallback);
            }
        }
    }

    public void removeConnectCallback(ConnectCallback connectCallback) {
        synchronized (mCallbackLock) {
            mConnectCallbacks.remove(connectCallback);
        }
    }

    public boolean connect(Context context, String address) {
        return connect(context, BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address));
    }

    public boolean connect(Context context, BluetoothDevice device) {
        LOG(TAG, "connect " + device + "; " + mConnState);
        synchronized (mStateLock) {
            if (mConnState != STATE_DISCONNECTED) {
                return true;
            }
            mConnState = STATE_CONNECTING;
        }
        mBluetoothGatt = device.connectGatt(context, false, mBluetoothGattCallback);
        return mBluetoothGatt != null;
    }

    public boolean discoverServices() {
        LOG(TAG , "discoverServices");
        if (mBluetoothGatt != null) {
            return mBluetoothGatt.discoverServices();
        }
        return false;
    }


    public boolean requestMtu(int mtu) {
        LOG(TAG , "requestMtu");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            LOG(TAG , "requestMtu < Build.VERSION_CODES.LOLLIPOP() not need");
            return false;
        }
        if (mBluetoothGatt != null) {
            return mBluetoothGatt.requestMtu(mtu);
        }
        return false;
    }

    public boolean enableCharacteristicNotify(UUID service, UUID rxCharacteristic, UUID descriptor) {
        LOG(TAG , "enableCharacteristicNotify()");
        if (mBluetoothGatt != null) {
            BluetoothGattService gattService = mBluetoothGatt.getService(service);
            if (gattService == null) {
                return false;
            }
            BluetoothGattCharacteristic gattCharacteristic = gattService.getCharacteristic(rxCharacteristic);
            if (gattCharacteristic == null) {
                return false;
            }
            BluetoothGattDescriptor gattDescriptor = gattCharacteristic.getDescriptor(descriptor);
            if (gattDescriptor == null) {
                return false;
            }
            if (!mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true)) {
                LOG(TAG , " enableCharacteristicNotify  mBluetoothGatt.setCharacteristicNotification(gattCharacteristic, true) is false");
                return false;
            }
            if (!gattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)) {
                LOG(TAG , " enableCharacteristicNotify  gattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) is false");
                return false;
            }
            mDescriptor = descriptor;
            return mBluetoothGatt.writeDescriptor(gattDescriptor);
        }
        return false;
    }

    public void close() {
        LOG(TAG , "close()");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
        }
        notifyConnectionStateChanged(false);
        mBluetoothGatt = null;
    }

    public boolean isConnected() {
        return mConnState == STATE_CONNECTED;
    }

    public boolean refresh() {
        try {
            if (mBluetoothGatt != null) {
                Method refresh = mBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                return ((Boolean) refresh.invoke(mBluetoothGatt, new Object[0])).booleanValue();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean setWriteCharacteristic(UUID service, UUID characteristic) {
        LOG(TAG, "setWriteCharacteristic service " + service.toString() + "; characteristic " + characteristic.toString());
        if (mBluetoothGatt == null) {
            return false;
        }
        BluetoothGattService gattService = mBluetoothGatt.getService(service);
        if (gattService == null) {
            return false;
        }
        mCharacteristicTx = gattService.getCharacteristic(characteristic);
        if (mCharacteristicTx == null) {
            return false;
        }
        mCharacteristicTx.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        return true;
    }

    public boolean write(byte[] data) {
        if (mBluetoothGatt != null) {
            mCharacteristicTx.setValue(data);
            mCharacteristicTx.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
            boolean ret = mBluetoothGatt.writeCharacteristic(mCharacteristicTx);
            return ret ;
        }
        LOG(TAG, "write  (mBluetoothGatt == null)" );
        return false;
    }

    public boolean write_no_rsp(byte[] data) {
        if (mBluetoothGatt != null) {
            mCharacteristicTx.setValue(data);
            mCharacteristicTx.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            boolean ret = mBluetoothGatt.writeCharacteristic(mCharacteristicTx);
            return ret ;
        }
        LOG(TAG, "write  (mBluetoothGatt == null)" );
        return false;
    }

    private void notifyConnectionStateChanged(boolean connected) {
        synchronized (mStateLock) {
            if (connected && mConnState != STATE_CONNECTED) {
                for (ConnectCallback callback : mConnectCallbacks) {
                    callback.onConnectionStateChanged(true);
                }
                mConnState = STATE_CONNECTED;
            } else if (!connected && mConnState != STATE_DISCONNECTED) {
                for (ConnectCallback callback : mConnectCallbacks) {
                    callback.onConnectionStateChanged(false);
                }
                mConnState = STATE_DISCONNECTED;
            }
        }
    }

    private void notifyServicesDiscovered(int status) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks) {
                if (callback instanceof LeConnectCallback) {
                    ((LeConnectCallback) callback).onServicesDiscovered(status);
                }
            }
        }
    }

    private void notifyCharacteristicNotifyEnabled(int status) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks) {
                if (callback instanceof LeConnectCallback) {
                    ((LeConnectCallback) callback).onCharacteristicNotifyEnabled(status);
                }
            }
        }
    }

    private void notifyMtuChanged(int status, int mtu) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks) {
                if (callback instanceof LeConnectCallback) {
                    ((LeConnectCallback) callback).onMtuChanged(status, mtu);
                }
            }
        }
    }

    private void notifyWrite(int status) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks) {
                if (callback instanceof LeConnectCallback) {
                    ((LeConnectCallback) callback).onWritten(status);
                }
            }
        }
    }

    private void notifyReceive(byte[] data) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks) {
                callback.onReceive(data);
            }
        }
    }

    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            LOG(TAG, "onConnectionStateChange " + status + "; " + newState);
            mBluetoothGatt = gatt;
            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothGatt.STATE_CONNECTED) {
                notifyConnectionStateChanged(true);
            } else {
                close();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            LOG(TAG, "onServicesDiscovered " + status + "; " + status);
            mBluetoothGatt = gatt;
            if (status != BluetoothGatt.GATT_SUCCESS) {
                notifyServicesDiscovered(LE_ERROR);
                refresh();
                close();
            } else {
                notifyServicesDiscovered(LE_SUCCESS);
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            LOG(TAG, "onCharacteristicWrite status is "  + status+ ArrayUtil.toHex(characteristic.getValue())); //打印等效与延时。故不能打印太多在此处。
            if (status == BluetoothGatt.GATT_SUCCESS) {
                notifyWrite(LE_SUCCESS);
            } else {
                notifyWrite(LE_ERROR);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            if(characteristic != null){
                LOG(TAG, "onCharacteristicChanged "+ ArrayUtil.toHex(characteristic.getValue()));
            }else{
                LOG(TAG, "onCharacteristicChanged characteristic is null");
            }
            notifyReceive(characteristic.getValue());
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            LOG(TAG, "onDescriptorWrite status is "+status);
            if (descriptor.getUuid().equals(mDescriptor)) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    notifyCharacteristicNotifyEnabled(LE_SUCCESS);
                } else {
                    notifyCharacteristicNotifyEnabled(LE_ERROR);
                }
            }
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            LOG(TAG, "onMtuChanged " + mtu + "; " + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                notifyMtuChanged(LE_SUCCESS, mtu);
            } else {
                notifyMtuChanged(LE_ERROR, mtu);
            }
        }
    };

    protected void LOG(String tag , String msg){
        if(tag != null && msg != null){
           // LogUtils.writeForBle(tag , msg);
        }
    }
}
