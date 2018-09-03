package com.gy.android.flowmusic.presenter;

import com.gy.android.flowmusic.http.RetrofitHelper;
import com.gy.android.flowmusic.model.SearchSong;
import com.gy.android.flowmusic.service.MusicService;
import com.gy.android.flowmusic.view.ISearchView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter {
    private ISearchView searchView;
    private MusicService service = RetrofitHelper.getInstance().createService(MusicService.class);

    public SearchPresenter(ISearchView searchView) {
        this.searchView = searchView;
    }

    public void search(String query) {
        searchView.showLoading();
        Call<SearchSong> call = service.getSearchSong(query);
        call.enqueue(new Callback<SearchSong>() {
            @Override
            public void onResponse(Call<SearchSong> call, Response<SearchSong> response) {
                searchView.dismissLoading();
                searchView.success(response.body());
            }

            @Override
            public void onFailure(Call<SearchSong> call, Throwable t) {
                searchView.dismissLoading();
                searchView.error(t.getMessage());
            }
        });
    }
}
