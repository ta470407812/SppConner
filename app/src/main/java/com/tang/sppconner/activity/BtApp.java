package com.tang.sppconner.activity;

import android.app.Application;

import com.tang.sppconner.config.BtConfig;
import com.tang.sppconner.utils.BytesUtils;
import com.tang.sppconner.utils.SimpleLog;
import com.tencent.bugly.crashreport.CrashReport;

import io.realm.Realm;
import pl.tajchert.nammu.Nammu;

public class BtApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Nammu.init(this);
        Realm.init(this);
        CrashReport.initCrashReport(getApplicationContext(), BtConfig.BUGLY_ID, BtConfig.IS_DEBUG);
    }
}
