package com.tang.sppconner.activity;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tang.sppconner.R;

public class MainActivity extends BaseBtServiceActivity implements
        View.OnClickListener,
        PermissionCallback,
        BtService.IBtService {

    private TextView btn_location;
    private TextView btn_conn_state;

    private final byte UPDATE_CONN_STATE = 0x02;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONN_STATE:
                    updateConnState();
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_location = findViewById(R.id.btn_location);
        btn_conn_state = findViewById(R.id.btn_conn_state);

        btn_location.setOnClickListener(this);
        btn_location.setText(Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                ? "获取定位权限成功"
                : "获取定位权限失败");
    }

    @Override
    protected void onDestroy() {
        if (null != handler) {
            handler.removeMessages(UPDATE_CONN_STATE);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    protected void serviceConnected(BtService service) {
        service.addIBtService(this);
        handler.sendEmptyMessage(UPDATE_CONN_STATE);
    }

    @Override
    protected void serviceWillDisconnect(BtService service) {
        service.removeIBtService(this);
    }

    @Override
    protected void serviceDisconnected() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                Nammu.askForPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, this);
                break;
        }
    }

    @Override
    public void permissionGranted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_location.setText(Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        ? "获取定位权限成功"
                        : "获取定位权限失败");
            }
        });
    }

    @Override
    public void permissionRefused() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_location.setText(Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        ? "获取定位权限成功"
                        : "获取定位权限失败");
            }
        });
    }

    @Override
    public void onConnectionStateChanged(boolean connected) {
        handler.sendEmptyMessage(UPDATE_CONN_STATE);
    }

    private void updateConnState() {
        if (null == btService)
            return;
        btn_conn_state.setText(btService.isConnected()
                ? "连接成功"
                : "连接失败");
        btn_conn_state.setTextColor(btService.isConnected()
                ? Color.GREEN
                : Color.RED);
    }
}
