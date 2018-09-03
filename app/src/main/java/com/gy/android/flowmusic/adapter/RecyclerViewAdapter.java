package com.gy.android.flowmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gy.android.flowmusic.R;
import com.gy.android.flowmusic.constants.Constant;
import com.gy.android.flowmusic.model.Lrc;
import com.gy.android.flowmusic.model.PlaySong;
import com.gy.android.flowmusic.model.RecommendMusic;
import com.gy.android.flowmusic.presenter.LrcPresenter;
import com.gy.android.flowmusic.presenter.PlaySongPresenter;
import com.gy.android.flowmusic.utils.LoadingDialog;
import com.gy.android.flowmusic.utils.MusicUtils;
import com.gy.android.flowmusic.utils.NetWorkUtils;
import com.gy.android.flowmusic.utils.ToastUtils;
import com.gy.android.flowmusic.view.ILrcView;
import com.gy.android.flowmusic.view.IPlaySongView;
import com.gy.android.flowmusic.widget.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements IPlaySongView, ILrcView {
    private List<RecommendMusic.ResultBean.ListBean> list;
    private Context mContext;
    private boolean flag = true;
    private LoadingDialog loadingDialog;
    private int currentPosition;
    private boolean isFirst = true;
    private IPositionListener positionListener;
    private LrcPresenter lrcPresenter = new LrcPresenter(this);
    private PlaySongPresenter playSongPresenter = new PlaySongPresenter(this);

    public RecyclerViewAdapter(List<RecommendMusic.ResultBean.ListBean> list, Context mContext, IPositionListener positionListener) {
        this.list = list;
        this.mContext = mContext;
        this.positionListener = positionListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        RecommendMusic.ResultBean.ListBean listBean = list.get(position);
        if (NetWorkUtils.checkNetworkState(mContext)) {
            Picasso.get().load(listBean.getPic_big()).error(R.mipmap.icon).placeholder(R.mipmap.icon).into(holder.music_bg);
            Picasso.get().load(listBean.getPic_small()).error(R.mipmap.icon).placeholder(R.mipmap.icon).into(holder.music_image);
            holder.music_singer.setText(listBean.getAuthor());
            holder.music_title.setText(listBean.getTitle());
            holder.music_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //如果是首次点击
                    if (isFirst) {
                        holder.music_play.setBackgroundResource(R.mipmap.music_stop);
                        if (NetWorkUtils.checkNetworkState(mContext)) {
                            positionListener.initPlayBar(listBean);
                            playSongPresenter.playSong(listBean.getSong_id());
                            lrcPresenter.getLrc(listBean.getSong_id());
                            flag = false;
                        } else {
                            ToastUtils.showShort(Constant.NET_EXCEPTION);
                        }
                        currentPosition = position;
                        isFirst = false;
                    } else {
                        //判断当前点击的位置和上一次的位置比较
                        if (currentPosition == position) {
                            if (flag) {
                                holder.music_play.setBackgroundResource(R.mipmap.music_stop);
                                if (NetWorkUtils.checkNetworkState(mContext)) {
                                    positionListener.initPlayBar(listBean);
                                    playSongPresenter.playSong(listBean.getSong_id());
                                    lrcPresenter.getLrc(listBean.getSong_id());
                                    flag = false;
                                } else {
                                    ToastUtils.showShort(Constant.NET_EXCEPTION);
                                }
                                currentPosition = position;
                            } else {
                                holder.music_play.setBackgroundResource(R.mipmap.music_play);
                                MusicUtils.pause();
                                flag = true;
                            }
                        } else {
                            holder.music_play.setBackgroundResource(R.mipmap.music_stop);
                            if (NetWorkUtils.checkNetworkState(mContext)) {
                                positionListener.getPosition(currentPosition);
                                positionListener.initPlayBar(listBean);
                                playSongPresenter.playSong(listBean.getSong_id());
                                lrcPresenter.getLrc(listBean.getSong_id());
                                flag = false;
                            } else {
                                ToastUtils.showShort(Constant.NET_EXCEPTION);
                            }
                            currentPosition = position;
                        }
                    }
                }
            });
        } else {
            ToastUtils.showShort(Constant.NET_EXCEPTION);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void success(PlaySong.BitrateBean bitrateBean) {
        MusicUtils.play(bitrateBean.getFile_link());
    }

    @Override
    public void error(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(mContext);
        } else {
            loadingDialog.show();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            //payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, position);
        } else {
            holder.music_play.setBackgroundResource(R.mipmap.music_play);
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
    public void success(Lrc lrc) {
        positionListener.play(lrc);
    }

    public interface IPositionListener {
        void getPosition(int position);

        void initPlayBar(RecommendMusic.ResultBean.ListBean listBean);

        void play(Lrc lrc);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public final ImageView music_bg;
        public final CircleImageView music_image;
        public final TextView music_singer;
        public final TextView music_title;
        public final TextView music_play;

        MyViewHolder(View itemView) {
            super(itemView);
            music_bg = itemView.findViewById(R.id.music_bg);
            music_image = itemView.findViewById(R.id.music_image);
            music_singer = itemView.findViewById(R.id.music_singer);
            music_title = itemView.findViewById(R.id.music_title);
            music_play = itemView.findViewById(R.id.music_play);
        }
    }
}
