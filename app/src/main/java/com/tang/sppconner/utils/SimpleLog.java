package com.tang.sppconner.utils;

import android.util.Log;

public class SimpleLog {
    private static final String TAG = "SimpleLog";
    private static final boolean IS_BUG = true;

    public static void print(Class<?> cls, String txt) {
        if (IS_BUG)
            Log.e(cls.getName(), txt);
    }

    public static void print(Class<?> cls, String... txts) {
        if (null == txts
                || txts.length == 0)
            return;
        if (IS_BUG) {
            StringBuilder stringBuilder = new StringBuilder("");
            for (String txt : txts) {
                stringBuilder.append(txt).append(" ");
            }
            print(cls, stringBuilder.toString());
        }
    }
}
