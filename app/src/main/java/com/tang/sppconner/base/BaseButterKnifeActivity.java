package com.tang.sppconner.base;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import butterknife.ButterKnife;

public abstract class BaseButterKnifeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        ButterKnife.bind(this);
        initView();
        initData();
    }

    protected abstract @LayoutRes
    int getLayoutResID();

    protected abstract void initView();

    protected abstract void initData();
}
