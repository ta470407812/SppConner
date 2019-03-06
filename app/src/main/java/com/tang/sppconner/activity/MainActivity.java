package com.tang.sppconner.activity;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tang.sppconner.R;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        PermissionCallback {

    private TextView btn_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_location = findViewById(R.id.btn_location);
        btn_location.setOnClickListener(this);
        btn_location.setText(Nammu.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                ? "获取定位权限成功"
                : "获取定位权限失败");
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
}
