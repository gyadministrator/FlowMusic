package com.gy.android.flowmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gy.android.flowmusic.R;
import com.gy.android.flowmusic.adapter.RecyclerViewAdapter;
import com.gy.android.flowmusic.constants.Constant;
import com.gy.android.flowmusic.model.Lrc;
import com.gy.android.flowmusic.model.PlaySong;
import com.gy.android.flowmusic.model.RecommendMusic;
import com.gy.android.flowmusic.presenter.MainPresenter;
import com.gy.android.flowmusic.utils.ImmersedStatusbarUtils;
import com.gy.android.flowmusic.utils.LoadingDialog;
import com.gy.android.flowmusic.utils.MusicUtils;
import com.gy.android.flowmusic.utils.NetWorkUtils;
import com.gy.android.flowmusic.utils.ToastUtils;
import com.gy.android.flowmusic.view.IMainView;
import com.gy.android.flowmusic.widget.CircleImageView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends BaseActivity implements IMainView, XRecyclerView.LoadingListener, View.OnClickListener, RecyclerViewAdapter.IPositionListener {
    private static final String TAG = "MainActivity";
    private XRecyclerView recyclerView;
    private TextView search;
    private MainPresenter mainPresenter;
    private LoadingDialog loadingDialog;
    private Integer num = 10;
    private FloatingActionButton top;
    private Button reload;
    private RelativeLayout no_rel;
    private LinearLayout play_bar;
    private CircleImageView bar_image;
    private TextView bar_play;
    private TextView bar_title;
    private TextView bar_lrc;
    private LinearLayout lin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mainPresenter = new MainPresenter(this);
        recyclerView.setLoadingListener(this);
        /*设置沉侵式导航栏*/
        ImmersedStatusbarUtils.initAfterSetContentView(this, lin);
        initData(num);
    }

    private void initData(Integer num) {
        if (NetWorkUtils.checkNetworkState(this)) {
            mainPresenter.initData(num);
        } else {
            ToastUtils.showShort(Constant.NET_EXCEPTION);
            no_rel.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.SquareSpin);
        search = findViewById(R.id.search);
        search.setOnClickListener(this);
        top = findViewById(R.id.top);
        reload = findViewById(R.id.reload);
        reload.setOnClickListener(this);
        no_rel = findViewById(R.id.no_rel);
        play_bar = findViewById(R.id.play_bar);
        bar_image = findViewById(R.id.bar_image);
        bar_play = findViewById(R.id.bar_play);
        bar_title = findViewById(R.id.bar_title);
        bar_lrc = findViewById(R.id.bar_lrc);
        lin=findViewById(R.id.lin);
    }

    @Override
    public void success(List<RecommendMusic.ResultBean.ListBean> t) {
        recyclerView.refreshComplete();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(new RecyclerViewAdapter(t, this, this));

        top.setVisibility(View.VISIBLE);
        no_rel.setVisibility(View.GONE);
        play_bar.setVisibility(View.VISIBLE);
        top.setOnClickListener(this);
    }

    @Override
    public void error(String msg) {
        recyclerView.loadMoreComplete();
        recyclerView.refreshComplete();
        ToastUtils.showShort(msg);
        play_bar.setVisibility(View.GONE);
        top.setVisibility(View.GONE);
        no_rel.setVisibility(View.VISIBLE);
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
    public void onRefresh() {
        num = 10;
        initData(num);
    }

    @Override
    public void onLoadMore() {
        num = num + 10;
        initData(num);
        recyclerView.loadMoreComplete();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top:
                //回到顶部
                recyclerView.scrollToPosition(0);
                break;
            case R.id.reload:
                initData(num);
                break;
            case R.id.bar_play:
                if (MusicUtils.mediaPlayer.isPlaying()) {
                    MusicUtils.pause();
                    bar_play.setBackgroundResource(R.mipmap.music_play);
                } else {
                    MusicUtils.playContinue();
                    bar_play.setBackgroundResource(R.mipmap.music_stop);
                }
                break;
            case R.id.search:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData(num);
    }

    @Override
    public void getPosition(int position) {
        View view = recyclerView.getChildAt(position);
        assert recyclerView.getChildViewHolder(view) != null;
        RecyclerViewAdapter.MyViewHolder viewHolder = (RecyclerViewAdapter.MyViewHolder) recyclerView.getChildViewHolder(view);
        if (null != viewHolder) {
            viewHolder.music_play.setBackgroundResource(R.mipmap.music_play);
        }
    }

    @Override
    public void initPlayBar(RecommendMusic.ResultBean.ListBean listBean) {
        Picasso.get().load(listBean.getPic_big()).placeholder(R.mipmap.icon).error(R.mipmap.icon)
                .into(bar_image);
        bar_play.setBackgroundResource(R.mipmap.music_stop);
        bar_title.setText(listBean.getTitle());

        bar_play.setOnClickListener(this);

    }

    @Override
    public void play(Lrc lrc) {
        bar_lrc.setText(lrc.getLrcContent());
        bar_lrc.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
