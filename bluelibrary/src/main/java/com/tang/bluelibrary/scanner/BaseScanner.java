package com.tang.bluelibrary.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.tang.bluelibrary.BtHelper;
import com.tang.bluelibrary.callback.ScanCallback;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public abstract class BaseScanner implements BtScanner {

    protected final String TAG = getClass().getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;

    private ScanCallback mScanCallback;

    private boolean mScanning = false;

    public BaseScanner(Context context) {
        mBluetoothAdapter = BtHelper.getBluetoothAdapter(context);
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    @Override
    public void startScan(ScanCallback callback) {
        mScanCallback = callback;
    }

    @Override
    public void close() {
        mScanCallback = null;
    }

    protected void onFound(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (mScanCallback != null) {
            mScanCallback.onFound(device, rssi, scanRecord);
        }
    }

    protected void onScanStart() {
        mScanning = true;
        if (mScanCallback != null) {
            mScanCallback.onScanStart();
        }
    }

    protected void onScanFinish() {
        mScanning = false;
        if (mScanCallback != null) {
            mScanCallback.onScanFinish();
        }
    }

    protected boolean isScanning() {
        return mScanning;
    }
}
