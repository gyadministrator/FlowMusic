package com.gy.android.flowmusic.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gy.android.flowmusic.R;
import com.gy.android.flowmusic.adapter.ListAdapter;
import com.gy.android.flowmusic.constants.Constant;
import com.gy.android.flowmusic.model.PlaySong;
import com.gy.android.flowmusic.model.SearchSong;
import com.gy.android.flowmusic.presenter.PlaySongPresenter;
import com.gy.android.flowmusic.presenter.SearchPresenter;
import com.gy.android.flowmusic.utils.ImmersedStatusbarUtils;
import com.gy.android.flowmusic.utils.LoadingDialog;
import com.gy.android.flowmusic.utils.MusicUtils;
import com.gy.android.flowmusic.utils.NetWorkUtils;
import com.gy.android.flowmusic.utils.ToastUtils;
import com.gy.android.flowmusic.view.IPlaySongView;
import com.gy.android.flowmusic.view.ISearchView;

import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener, ISearchView, AdapterView.OnItemClickListener, IPlaySongView {
    private TextView back;
    private EditText key;
    private TextView ok;
    private ListView listView;
    private SearchPresenter searchPresenter;
    private LoadingDialog loadingDialog;
    private ListAdapter listAdapter;
    private PlaySongPresenter playSongPresenter;
    private List<SearchSong.SongBean> list;
    private LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchPresenter = new SearchPresenter(this);
        playSongPresenter = new PlaySongPresenter(this);
        /*设置沉侵式导航栏*/
        ImmersedStatusbarUtils.initAfterSetContentView(this, lin);
        initView();
    }

    private void initView() {
        back = findViewById(R.id.back);
        key = findViewById(R.id.key);
        ok = findViewById(R.id.ok);
        listView = findViewById(R.id.listView);
        lin = findViewById(R.id.lin);

        back.setOnClickListener(this);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                String query = key.getText().toString();
                if ("".equals(query)) {
                    ToastUtils.showShort("请输入关键字");
                } else {
                    if (NetWorkUtils.checkNetworkState(this)) {
                        searchPresenter.search(query);
                    } else {
                        ToastUtils.showShort(Constant.NET_EXCEPTION);
                    }
                }
                break;
        }
    }

    @Override
    public void success(SearchSong searchSong) {
        list = searchSong.getSong();
        listAdapter = new ListAdapter(searchSong.getSong(), this);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void error(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
    }

    @Override
    public void dismissLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SearchSong.SongBean songBean = list.get(i);
        if (NetWorkUtils.checkNetworkState(this)) {
            if (MusicUtils.mediaPlayer != null) {
                if (MusicUtils.mediaPlayer.isPlaying()) {
                    MusicUtils.pause();
                } else {
                    playSongPresenter.playSong(songBean.getSongid());
                }
            } else {
                playSongPresenter.playSong(songBean.getSongid());
            }
        } else {
            ToastUtils.showShort(Constant.NET_EXCEPTION);
        }
    }

    @Override
    public void success(PlaySong.BitrateBean bitrateBean) {
        MusicUtils.play(bitrateBean.getFile_link());
    }
}
