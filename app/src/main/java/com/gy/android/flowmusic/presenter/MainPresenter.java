package com.gy.android.flowmusic.presenter;

import android.util.Log;

import com.gy.android.flowmusic.http.RetrofitHelper;
import com.gy.android.flowmusic.model.RecommendMusic;
import com.gy.android.flowmusic.service.MusicService;
import com.gy.android.flowmusic.view.IMainView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {
    private static final String TAG = "MainPresenter";
    private IMainView mainView;
    private final RetrofitHelper retrofitHelper = RetrofitHelper.getInstance();
    private final MusicService service = retrofitHelper.createService(MusicService.class);

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;
    }

    public void initData(Integer num) {
        mainView.showLoading();
        Call<RecommendMusic> call = service.getRecommendMusic("877578", num);
        call.enqueue(new Callback<RecommendMusic>() {
            @Override
            public void onResponse(Call<RecommendMusic> call, Response<RecommendMusic> response) {
                mainView.dismissLoading();
                assert response.body() != null;
                if (response.body().getResult().getList() == null) {
                    mainView.error("没有数据了...");
                } else {
                    mainView.success(response.body().getResult().getList());
                }
            }

            @Override
            public void onFailure(Call<RecommendMusic> call, Throwable t) {
                mainView.dismissLoading();
                mainView.showMessage(t.getMessage());
                mainView.error(t.getMessage());
            }
        });
    }
}
