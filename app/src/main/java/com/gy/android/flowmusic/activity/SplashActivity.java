package com.gy.android.flowmusic.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telecom.TelecomManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.gy.android.flowmusic.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SplashActivity extends BaseActivity {
    private ImageView splash_image;
    private TextView copyRight;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splash_image = findViewById(R.id.splash_image);
        copyRight = findViewById(R.id.copyRight);


        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy");
        String t = format.format(new Date());
        if (!"2018".equals(t)) {
            copyRight.setText(R.string.copyright + "-" + t);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
