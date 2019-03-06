package com.tang.sppconner.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tang on 16/9/23.
 */

public class ToastUtils {
    public static void showShort(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, int string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int string) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }
}
