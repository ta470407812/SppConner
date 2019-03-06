package com.tang.bluelibrary.scanner;

import android.content.Context;
import android.os.Build;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public class LeScannerCompat {

    public static BtScanner getLeScanner(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return new LeJBScanner(context);
        } else {
            return new LeLollipopScanner(context);
        }
    }
}
