package com.tang.sppconner.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseBtServiceActivity extends AppCompatActivity {
    protected BtService btService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        bindManagerService(this);
    }

    @Override
    protected void onDestroy() {
        serviceWillDisconnect(btService);
        unbindManagerService();
        super.onDestroy();
    }


    private ServiceConnection bleConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BtService.BtBinder binder = (BtService.BtBinder) service;
            btService = binder.getService();
            serviceConnected(btService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceDisconnected();
            //System.gc();
        }
    };

    private boolean bindManagerService(Context contxt) {
        Intent intent = new Intent(this, BtService.class);
        return contxt.bindService(intent, bleConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void unbindManagerService() {
        unbindService(bleConnection);
    }

    protected abstract void serviceConnected(BtService service);

    protected abstract void serviceWillDisconnect(BtService service);

    protected abstract void serviceDisconnected();
}
