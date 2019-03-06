package com.tang.sppconner.activity;

import android.app.Application;

import com.tang.sppconner.utils.BytesUtils;
import com.tang.sppconner.utils.SimpleLog;

import pl.tajchert.nammu.Nammu;

public class BtApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Nammu.init(this);
        String hex = "ab,dc,fe,02,00,00,01,00,25,ee";
        SimpleLog.print(this.getClass(), "count " + BytesUtils.hex2Bytes(hex).length);
    }
}
