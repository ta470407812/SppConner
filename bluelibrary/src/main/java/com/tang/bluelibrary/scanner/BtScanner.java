package com.tang.bluelibrary.scanner;

import com.tang.bluelibrary.callback.ScanCallback;

/**
 * Created by zhaowanxing on 2017/4/17.
 */

public interface BtScanner {

    void startScan(ScanCallback callback);

    void stopScan();

    void close();
}
