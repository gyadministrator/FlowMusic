package com.gy.android.flowmusic.presenter;

import android.support.annotation.NonNull;

import com.gy.android.flowmusic.http.RetrofitHelper;
import com.gy.android.flowmusic.model.PlaySong;
import com.gy.android.flowmusic.service.MusicService;
import com.gy.android.flowmusic.view.IMainView;
import com.gy.android.flowmusic.view.IPlaySongView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaySongPresenter {
    private IPlaySongView playSongView;
    private MusicService service = RetrofitHelper.getInstance().createService(MusicService.class);

    public PlaySongPresenter(IPlaySongView playSongView) {
        this.playSongView = playSongView;
    }

    public void playSong(String songid) {
        playSongView.showLoading();
        Call<PlaySong> call = service.getPlaySong(songid);
        call.enqueue(new Callback<PlaySong>() {
            @Override
            public void onResponse(Call<PlaySong> call, Response<PlaySong> response) {
                playSongView.dismissLoading();
                assert response.body() != null;
                playSongView.success(response.body().getBitrate());
            }

            @Override
            public void onFailure(@NonNull Call<PlaySong> call, Throwable t) {
                playSongView.dismissLoading();
                playSongView.error(t.getMessage());
            }
        });


    }
}
