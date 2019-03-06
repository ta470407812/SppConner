package com.tang.sppconner.activity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tang.bluelibrary.ArrayUtil;
import com.tang.bluelibrary.SppConnector;
import com.tang.bluelibrary.callback.ConnectCallback;
import com.tang.sppconner.config.BtConfig;
import com.tang.sppconner.utils.BytesUtils;
import com.tang.sppconner.utils.SimpleLog;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

public class BtService extends Service implements ConnectCallback {

    private SppConnector sppConnector;
    private BluetoothAdapter bluetoothAdapter;
    private boolean connected;
    private CopyOnWriteArrayList<IBtService> iBtServiceList = new CopyOnWriteArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        sppConnector = new SppConnector();
        sppConnector.addConnectCallback(this);
    }

    @Override
    public void onDestroy() {
        iBtServiceList.clear();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SimpleLog.print(this.getClass(), "onStartCommand " + flags);
        onReceiverHandler(intent);
        return START_STICKY;
    }

    private final BtBinder btBinder = new BtBinder();

    public class BtBinder extends Binder {
        public BtService getService() {
            return BtService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return btBinder;
    }

    private void onReceiverHandler(Intent intent) {
        // String action = intent.getAction();
        String action = intent.getStringExtra("action");
        SimpleLog.print(this.getClass(), "onReceiverHandler " + action);
        if (null != action)
            switch (action) {
                case BtConfig
                        .Action.Open_Bluetooth: {
                    if (!bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.enable();
                    }
                }
                break;
                case BtConfig
                        .Action.Close_Bluetooth: {
                    if (bluetoothAdapter.isEnabled()) {
                        bluetoothAdapter.disable();
                    }
                }
                break;
                case BtConfig
                        .Action.Conn_Spp: {
                    if (bluetoothAdapter.isEnabled()) {
                        String uuid = intent.getStringExtra(BtConfig.Key.UUID);
                        if (BluetoothAdapter.checkBluetoothAddress(uuid)) {
                            sppConnector.connect(bluetoothAdapter.getRemoteDevice(uuid), true);
                        }
                    }
                }
                break;
                case BtConfig
                        .Action.Send_Cmd: {
                    String cmd = intent.getStringExtra(BtConfig.Key.Cmd);
                    byte[] cmdBytes = BytesUtils.hex2Bytes(cmd);
                    if (null == cmdBytes
                            || cmdBytes.length == 0)
                        return;
                    if (bluetoothAdapter.isEnabled()) {
                        if (sppConnector.isConningState()) {
                            SimpleLog.print(this.getClass(), "write " + cmd);
                            sppConnector.write(cmdBytes);
                        }
                    }
                }
                break;
                default:
                    break;
            }
    }

    @Override
    public void onConnectionStateChanged(boolean connected) {
        this.connected = connected;
        SimpleLog.print(this.getClass(), "onConnectionStateChanged " + connected);
        for (IBtService iBtService : iBtServiceList) {
            iBtService.onConnectionStateChanged(connected);
        }
    }

    @Override
    public void onReceive(byte[] data) {
        SimpleLog.print(this.getClass(), "spp onReceive " + ArrayUtil.toHex(data));
    }

    public void addIBtService(IBtService iBtService) {
        if (null != iBtService
                && !iBtServiceList.contains(iBtService)) {
            iBtServiceList.add(iBtService);
        }
    }

    public void removeIBtService(IBtService iBtService) {
        if (null != iBtService) {
            iBtServiceList.remove(iBtService);
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public interface IBtService {
        void onConnectionStateChanged(boolean connected);
    }
}
