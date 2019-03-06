package com.tang.sppconner.activity;

import android.app.Application;

import com.tang.sppconner.utils.BytesUtils;
import com.tang.sppconner.utils.SimpleLog;

import io.realm.Realm;
import pl.tajchert.nammu.Nammu;

public class BtApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Nammu.init(this);
        Realm.init(this);
    }
}
