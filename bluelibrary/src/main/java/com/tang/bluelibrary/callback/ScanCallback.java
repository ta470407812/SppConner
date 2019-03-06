package com.tang.bluelibrary.callback;

import android.bluetooth.BluetoothDevice;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public interface ScanCallback {

    void onFound(BluetoothDevice device, int rssi, byte[] scanRecord);

    void onScanStart();

    void onScanFinish();
}
