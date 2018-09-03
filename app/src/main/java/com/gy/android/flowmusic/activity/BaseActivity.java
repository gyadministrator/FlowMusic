package com.gy.android.flowmusic.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.gy.android.flowmusic.utils.ActivityController;
import com.gy.android.flowmusic.utils.ToastUtils;


/**
 * Created by 高运 on 2017/5/14.
 */
public class BaseActivity extends AppCompatActivity {
    private boolean flag = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && getClass().getName().equals(MainActivity.class.getName())) {
            if (!flag) {
                flag = true;
                ToastUtils.showShort("再按一次返回键回到桌面");
                new Handler().postDelayed(r, 2000);
                return true;
            } else {
                ActivityController.removeAllActivity();
                Process.killProcess(Process.myPid());
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            flag = false;
        }
    };
}
