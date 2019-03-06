package com.tang.bluelibrary.scanner;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.tang.bluelibrary.callback.ScanCallback;

/**
 * Created by zhaowanxing on 2017/5/8.
 */

public class ClassicScanner extends BaseScanner {

    private Context mContext;

    public ClassicScanner(Context context) {
        super(context);
        mContext = context;
        initReceiver();
    }

    @Override
    public void startScan(ScanCallback callback) {
        super.startScan(callback);
        if (isScanning())
            return;
        getBluetoothAdapter().startDiscovery();
    }

    @Override
    public void stopScan() {
        if (!isScanning())
            return;
        getBluetoothAdapter().cancelDiscovery();
    }

    @Override
    public void close() {
        mContext.unregisterReceiver(mReceiver);
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (null != action)
                switch (action) {
                    case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                        onScanStart();
                        break;
                    case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                        onScanFinish();
                        break;
                    case BluetoothDevice.ACTION_FOUND:
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) -100);
                        onFound(device, rssi, null);
                        break;
                }
        }
    };
}
