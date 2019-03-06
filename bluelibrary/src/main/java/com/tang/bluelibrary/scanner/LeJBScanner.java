package com.tang.bluelibrary.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.tang.bluelibrary.callback.ScanCallback;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public class LeJBScanner extends BaseScanner {

    public LeJBScanner(Context context) {
        super(context);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void startScan(ScanCallback callback) {
        if (isScanning()) {
            return;
        }
        if (getBluetoothAdapter().startLeScan(mLeScanCallback)) {
            onScanStart();
        } else {
            onScanFinish();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void stopScan() {
        if (!isScanning())
            return;
        getBluetoothAdapter().stopLeScan(mLeScanCallback);
        onScanFinish();
    }

    @Override
    public void close() {

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            onFound(device, rssi, scanRecord);
        }
    };
}
