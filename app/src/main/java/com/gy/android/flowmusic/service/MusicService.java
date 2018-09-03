package com.gy.android.flowmusic.service;

import com.gy.android.flowmusic.model.Lrc;
import com.gy.android.flowmusic.model.PlaySong;
import com.gy.android.flowmusic.model.RecommendMusic;
import com.gy.android.flowmusic.model.SearchSong;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusicService {
    /**
     * 获取推荐的音乐
     *
     * @return
     */
    @GET("GetRecommendSongList")
    Call<RecommendMusic> getRecommendMusic(@Query("song_id") String song_id, @Query("num") Integer num);

    /**
     * 获取音乐播放地址
     *
     * @param songid
     * @return
     */
    @GET("PlaySong")
    Call<PlaySong> getPlaySong(@Query("songid") String songid);

    /**
     * 获取歌词
     *
     * @param songid
     * @return
     */
    @GET("GetLrc")
    Call<Lrc> getLrc(@Query("songid") String songid);

    /**
     * 搜索歌曲
     *
     * @param query
     * @return
     */
    @GET("GetSearchSong")
    Call<SearchSong> getSearchSong(@Query("query") String query);
}
