package com.gy.android.flowmusic.view;

import com.gy.android.flowmusic.model.PlaySong;

public interface IPlaySongView extends IBaseView {
    void success(PlaySong.BitrateBean bitrateBean);
}
