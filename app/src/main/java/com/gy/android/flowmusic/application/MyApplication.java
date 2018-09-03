package com.gy.android.flowmusic.application;

import android.app.Application;

import com.gy.android.flowmusic.utils.ToastUtils;
import com.squareup.leakcanary.LeakCanary;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        initLeakCanary();
    }

    /**
     * 初始化内存泄漏检测
     */
    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
