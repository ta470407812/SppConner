package com.tang.bluelibrary.callback;

/**
 * Created by zhaowanxing on 2017/7/12.
 */

public interface LeConnectCallback extends ConnectCallback {

    void onServicesDiscovered(int status);

    void onCharacteristicNotifyEnabled(int status);

    void onWritten(int status);

    void onMtuChanged(int status, int mtu);
}
