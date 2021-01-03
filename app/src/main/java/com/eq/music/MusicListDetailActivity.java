package com.eq.music;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MusicListDetailActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    MyListViewAdapter adapter;
    ArrayList<Music> mList;
    MediaPlayer mp;
    ImageView ivPlay,ivNext,ivLast;
    TextView tv_singer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list_detail);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.iv_music);
        ivPlay = (ImageView) findViewById(R.id.iv_play);
        ivNext = (ImageView) findViewById(R.id.iv_next);
        ivLast = (ImageView) findViewById(R.id.iv_last);
        tv_singer = (TextView) findViewById(R.id.tv_singer);
        final Intent intent = getIntent();
        mList = (ArrayList<Music>) intent.getSerializableExtra("music");
        mp = new MediaPlayer();
        adapter = new MyListViewAdapter(mList,this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null) {
                    mp.pause();
                    ivPlay.setImageResource(R.mipmap.ic_play_btn_pause);
                }
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index + 1;
                if (index < mList.size()){
                    isPlayMusic();
                }else {
                    index = 0;
                    isPlayMusic();
                }
            }
        });
        ivLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = index - 1;
                if (index < 0){
                    index = 0;
                    isPlayMusic();
                } else {
                    isPlayMusic();
                }
            }
        });
    }

    int index;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        index = position;
        isPlayMusic();
    }

    private void isPlayMusic(){
        if (mp != null){
            plaMusic();
        }else {
            mp = new MediaPlayer();
            plaMusic();
        }
    }

    private void plaMusic(){
        mp.reset();
        try {
            String url = mList.get(index).getUrl();
            mp.setDataSource(url);
            mp.prepare();
            mp.start();
            ivPlay.setImageResource(R.mipmap.ic_play_btn_pause_pressed);
            tv_singer.setText(mList.get(index).getTitle());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

