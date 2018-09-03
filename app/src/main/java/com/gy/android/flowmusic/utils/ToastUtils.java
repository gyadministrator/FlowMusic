package com.gy.android.flowmusic.utils;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

/**
 * 自定义工具类 提示消息
 */
public class ToastUtils {
    private static Toast toast;

    private static Application sContext;

    public static void init(Application application) {
        sContext = application;
    }

    public static void showShort(CharSequence sequence) {
        if (toast == null) {
            toast = Toast.makeText(sContext, sequence, Toast.LENGTH_SHORT);
        } else {
            toast.setText(sequence);
        }
        toast.show();

    }
}
