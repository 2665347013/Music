package com.eq.music;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;


public class MyListVideoAdapte extends BaseAdapter {
    ArrayList<Video> videoList;
    Context context;
    int currentPosition;
    MediaPlayer mp;

    public MyListVideoAdapte(ArrayList<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
        mp = new MediaPlayer();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }
    public void destoryPlayer(){
        if (mp!=null){
            mp.stop();
            mp.release();
        }
    }



    @Override
    public int getCount() {
        return videoList != null ? videoList.size() : 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(videoList.get(position).getUrl(), MediaStore.Video.Thumbnails.MINI_KIND);
        if (convertView == null){
            convertView = View.inflate(context,R.layout.item_mainlv,null);
            hodler = new MyListVideoAdapte.ViewHodler();
            hodler.item_main_iv = (ImageView) convertView.findViewById(R.id.item_main_iv);
            hodler.item_main_tv_name = (TextView) convertView.findViewById(R.id.item_main_tv_name);
            hodler.surfaceView = (SurfaceView) convertView.findViewById(R.id.item_surface);
            hodler.item_main_iv.setImageBitmap(thumbnail);
            hodler.item_main_iv.setTag(position);
            convertView.setTag(hodler);
        }else {
            hodler = (MyListVideoAdapte.ViewHodler) convertView.getTag();
        }
        hodler.item_main_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos;
                pos = (Integer) v.getTag();
                currentPosition = pos;
                notifyDataSetChanged();
            }
        });
        if (currentPosition == position) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            hodler.surfaceView.setVisibility(View.VISIBLE);
            hodler.item_main_iv.setVisibility(View.INVISIBLE);
            SurfaceHolder surfaceHolder = hodler.surfaceView.getHolder();
            surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    mp.reset();
                    mp.setDisplay(holder);
                    try {
                        mp.setDataSource(videoList.get(position).getUrl());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                }
            });
        } else {
            hodler.surfaceView.setVisibility(View.INVISIBLE);
            hodler.item_main_iv.setVisibility(View.VISIBLE);
        }
        Video video = videoList.get(position);
        hodler.item_main_tv_name.setText(video.getTitle());


        return convertView;
    }
    class ViewHodler{
        ImageView item_main_iv;
        TextView item_main_tv_name;
        SurfaceView surfaceView;
    }

}
