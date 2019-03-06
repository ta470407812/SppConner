package com.tang.bluelibrary;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.tang.bluelibrary.callback.ConnectCallback;

import java.util.UUID;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public class LeManager {

    private static volatile LeManager sLeManager;

    private LeConnector mConnectorX;

    private LeManager() {
        mConnectorX = new LeConnector();
    }

    public static LeManager getLeManager() {
        if (sLeManager == null) {
            synchronized (LeManager.class) {
                if (sLeManager == null) {
                    sLeManager = new LeManager();
                }
            }
        }
        return sLeManager;
    }

    public void addConnectCallback(ConnectCallback connectCallback) {
        mConnectorX.addConnectCallback(connectCallback);
    }

    public void removeConnectCallback(ConnectCallback connectCallback) {
        mConnectorX.removeConnectCallback(connectCallback);
    }

    public boolean connect(Context context, BluetoothDevice device) {
        return mConnectorX.connect(context, device);
    }

    public boolean connect(Context context, String address) {
        return mConnectorX.connect(context, address);
    }

    public boolean discoverServices() {
        return mConnectorX.discoverServices();
    }

    public boolean requestMtu(int mtu) {
        return mConnectorX.requestMtu(mtu);
    }

    public boolean enableCharacteristicNotify(UUID service, UUID rxCharacteristic, UUID descriptor) {
        return mConnectorX.enableCharacteristicNotify(service, rxCharacteristic, descriptor);
    }

    public boolean setWriteCharacteristic(UUID service, UUID characteristic) {
        return mConnectorX.setWriteCharacteristic(service, characteristic);
    }


    public void close() {
        mConnectorX.close();
    }

    public boolean isConnected() {
        return mConnectorX.isConnected();
    }

    public boolean refresh() {
        return mConnectorX.refresh();
    }

    public boolean write(byte[] data) {
        return mConnectorX.write(data);
    }

    public boolean write_no_rsp(byte[] data) {
        return mConnectorX.write_no_rsp(data);
    }
}
