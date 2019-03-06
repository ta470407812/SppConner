package com.tang.bluelibrary.callback;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public interface ConnectCallback {

    void onConnectionStateChanged(boolean connected);

    void onReceive(byte[] data);
}
