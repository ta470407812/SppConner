package com.tang.bluelibrary;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import androidx.annotation.NonNull;
import android.util.Log;

import com.tang.bluelibrary.callback.ConnectCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhaowanxing on 2017/4/23.
 */

public class SppConnector {
    private final String TAG = getClass().getSimpleName();

    private static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static volatile SppConnector sConnector;//
    private BluetoothSocket mBluetoothSocket;

    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_DISCONNECTED = 0;

    private int mConnState = STATE_DISCONNECTED;
    private Object mCallbackLock = new Object();

    public static SppConnector getConnector() {
        if (sConnector == null) {
            synchronized (SppConnector.class) {
                if (sConnector == null) {
                    sConnector = new SppConnector();
                }
            }
        }
        return sConnector;
    }

    public SppConnector() {
        mConnectCallbacks = new ArrayList<>();
    }


    public boolean connect(@NonNull BluetoothDevice device, boolean insecureRfcomm) {
        if (mConnState == STATE_CONNECTING || mConnState == STATE_CONNECTED) {
            return false;
        }
        new Thread(new ConnectRunnable(device, insecureRfcomm)).start();
        return true;
    }

    private void changeDeviceState(int state) {
        this.mConnState = state;
      //  SimpleLog.print(this.getClass(), "changeDeviceState " + mConnState);
    }

    public void disconnect() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
            onConnectionStateChanged(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean write(byte[] data) {
        if (mConnectedRunnable != null) {
            return mConnectedRunnable.write(data);
        }
        return false;
    }

    private List<ConnectCallback> mConnectCallbacks = new ArrayList<>();

    public void addConnectCallback(ConnectCallback callback) {
        synchronized (mCallbackLock) {
            if (!mConnectCallbacks.contains(callback))
                mConnectCallbacks.add(callback);
        }
    }

    public void removeConnectCallback(ConnectCallback callback) {
        synchronized (mCallbackLock) {
            mConnectCallbacks.remove(callback);
        }
    }

    private void onConnectionStateChanged(boolean connected) {
        //SimpleLog.print(this.getClass(), "onConnectionStateChanged 6666 " + connected);
        synchronized (mCallbackLock) {
            if (connected && mConnState != STATE_CONNECTED) {
                for (ConnectCallback callback : mConnectCallbacks)
                    callback.onConnectionStateChanged(true);
                changeDeviceState(STATE_CONNECTED);
            } else if (!connected && mConnState != STATE_DISCONNECTED) {
                mBluetoothSocket = null;
                mConnectedRunnable = null;
                changeDeviceState(STATE_DISCONNECTED);
                for (ConnectCallback callback : mConnectCallbacks)
                    callback.onConnectionStateChanged(false);
            }
        }
    }

    public int getmConnState() {
        return mConnState;
    }

    public boolean isConningState() {
        return mConnState == STATE_CONNECTED;
    }

    public void setmConnState(int mConnState) {
        this.mConnState = mConnState;
    }

    private void onReceive(byte[] data) {
        synchronized (mCallbackLock) {
            for (ConnectCallback callback : mConnectCallbacks)
                callback.onReceive(data);
        }
    }

    private ConnectedRunnable mConnectedRunnable;

    private class ConnectRunnable implements Runnable {

        private BluetoothDevice mDevice;
        private boolean insecureRfcomm;

        public ConnectRunnable(BluetoothDevice device, boolean insecureRfcomm) {  //insecure 不安全的连接
            mDevice = device;
            this.insecureRfcomm = insecureRfcomm;
        }

        @Override
        public void run() {
            try {
              //  SimpleLog.print(this.getClass(), "STATE_CONNECTING");
                changeDeviceState(STATE_CONNECTING);
                if (insecureRfcomm) {
                    mBluetoothSocket = mDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                } else {
                    mBluetoothSocket = mDevice.createRfcommSocketToServiceRecord(SPP_UUID);
                }

                // mDevice.createRfcommSocketToServiceRecord()
                mBluetoothSocket.connect();
                mConnectedRunnable = new ConnectedRunnable(mBluetoothSocket.getInputStream(), mBluetoothSocket.getOutputStream());
                onConnectionStateChanged(true);
                new Thread(mConnectedRunnable).start();
            } catch (IOException e) {
                e.printStackTrace();
                onConnectionStateChanged(false);
            }
        }
    }

    private class ConnectedRunnable implements Runnable {

        private OutputStream mWrite;
        private InputStream mRead;

        public ConnectedRunnable(InputStream read, OutputStream write) {
            mRead = read;
            mWrite = write;
        }

        @Override
        public void run() {
            try {
                byte[] data = new byte[1024 * 1024];
                Log.e("SPP", "connected thread run");
                while (true) {
                    int length = mRead.read(data);
                    onReceive(ArrayUtil.extractBytes(data, 0, length));
                }
            } catch (IOException e) {
                e.printStackTrace();
                onConnectionStateChanged(false);
            } finally {
                try {
                    if (mRead != null)
                        mRead.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean write(byte[] data) {
            try {
                mWrite.write(data);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                onConnectionStateChanged(false);
            }
            return false;
        }
    }
}
