package com.gy.android.flowmusic.view;

import com.gy.android.flowmusic.model.RecommendMusic;

import java.util.List;

public interface IMainView extends IBaseView {
    void success(List<RecommendMusic.ResultBean.ListBean> t);
}
