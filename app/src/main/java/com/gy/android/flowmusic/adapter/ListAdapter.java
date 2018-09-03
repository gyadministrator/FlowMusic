package com.gy.android.flowmusic.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gy.android.flowmusic.R;
import com.gy.android.flowmusic.model.SearchSong;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    private List<SearchSong.SongBean> list;
    private Context context;

    public ListAdapter(List<SearchSong.SongBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SearchSong.SongBean songBean = list.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_list_view, null);
            viewHolder = new ViewHolder();
            viewHolder.title = view.findViewById(R.id.list_title);
            viewHolder.author = view.findViewById(R.id.list_author);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.author.setText(songBean.getArtistname());
        viewHolder.title.setText(songBean.getSongname());
        return view;
    }

    static class ViewHolder {
        TextView title;
        TextView author;
    }
}
