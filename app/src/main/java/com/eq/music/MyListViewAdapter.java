package com.eq.music;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 2020/6/21.
 */

public class MyListViewAdapter extends BaseAdapter {
    ArrayList<Music> musicList;
    Context context;


    public MyListViewAdapter(ArrayList<Music> musicList, Context context) {
        this.musicList = musicList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return musicList != null ? musicList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null){
            convertView = View.inflate(context,R.layout.item_listview,null);
            hodler = new ViewHodler();
            hodler.iv = (ImageView) convertView.findViewById(R.id.iv);
            hodler.tvMusicTitle = (TextView) convertView.findViewById(R.id.tv_music_title);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        Music music = musicList.get(position);
        hodler.tvMusicTitle.setText(music.getTitle());
        return convertView;
    }

    class ViewHodler{
        ImageView iv;
        TextView tvMusicTitle;
    }
}

