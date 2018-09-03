package com.gy.android.flowmusic.view;

public interface IBaseView {
    void error(String msg);

    void showLoading();

    void dismissLoading();

    void showMessage(String msg);
}
