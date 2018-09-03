package com.gy.android.flowmusic.presenter;

import com.gy.android.flowmusic.http.RetrofitHelper;
import com.gy.android.flowmusic.model.Lrc;
import com.gy.android.flowmusic.service.MusicService;
import com.gy.android.flowmusic.view.ILrcView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LrcPresenter {
    private ILrcView lrcView;
    private MusicService service = RetrofitHelper.getInstance().createService(MusicService.class);

    public LrcPresenter(ILrcView lrcView) {
        this.lrcView = lrcView;
    }

    public void getLrc(String songid) {
        lrcView.showLoading();
        Call<Lrc> call = service.getLrc(songid);
        call.enqueue(new Callback<Lrc>() {
            @Override
            public void onResponse(Call<Lrc> call, Response<Lrc> response) {
                lrcView.dismissLoading();
                lrcView.success(response.body());
            }

            @Override
            public void onFailure(Call<Lrc> call, Throwable t) {
                lrcView.dismissLoading();
                lrcView.error(t.getMessage());
            }
        });
    }
}
