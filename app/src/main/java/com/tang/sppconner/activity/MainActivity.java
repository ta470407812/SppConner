package com.tang.sppconner.activity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.tang.sppconner.R;
import com.tang.sppconner.base.BaseBtServiceActivity;
import com.tang.sppconner.utils.SimpleLog;

public class MainActivity extends BaseBtServiceActivity implements
        BtService.IBtService {

    @BindView(R.id.btn_location)
    TextView btn_location;
    @BindView(R.id.btn_conn_state)
    TextView btn_conn_state;

    private final byte UPDATE_CONN_STATE = 0x02;
    private final byte UPDATE_LOCATION = 0x03;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_CONN_STATE:
                    updateConnState();
                    break;
                case UPDATE_LOCATION:
                    btn_location.setText(Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            ? "获取定位权限成功"
                            : "获取定位权限失败");
                    break;
            }
            return true;
        }
    });

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        handler.sendEmptyMessage(UPDATE_LOCATION);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        if (null != handler) {
            handler.removeMessages(UPDATE_CONN_STATE);
            handler.removeMessages(UPDATE_LOCATION);
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

    @OnClick({R.id.btn_location,
            R.id.btn_conn_history,
            R.id.btn_cmd_history})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_location:
                Nammu.askForPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, permissionCallback);
                break;
            case R.id.btn_conn_history:
                startActivity(new Intent(this, BtConnHistoryActivity.class));
                break;
            case R.id.btn_cmd_history:
                startActivity(new Intent(this, CmdHistoryActivity.class));
                break;
        }
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

    private PermissionCallback permissionCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            SimpleLog.print(this.getClass(), "permissionGranted");
            handler.sendEmptyMessage(UPDATE_LOCATION);
        }

        @Override
        public void permissionRefused() {
            SimpleLog.print(this.getClass(), "permissionRefused");
            handler.sendEmptyMessage(UPDATE_LOCATION);
        }
    };
}
