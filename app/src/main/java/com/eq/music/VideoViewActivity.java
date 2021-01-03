package com.eq.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.widget.ListView;


import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class VideoViewActivity extends AppCompatActivity {
    private ListView listView;
    MyListVideoAdapte adapte;
    ArrayList<Video> vList;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        initView();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.main_lv);
        final Intent intent = getIntent();
        vList = (ArrayList<Video>) intent.getSerializableExtra("video");
        adapte = new MyListVideoAdapte(vList,this);
        mp = new MediaPlayer();
        listView.setAdapter(adapte);
        // listView.setOnItemClickListener(this);
    }

//    int index;
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        index = position;
//        isPlayVideo();
//    }
//
//    private void isPlayVideo(){
//        if (mp != null){
//            plaVideo();
//        }else {
//            mp = new MediaPlayer();
//            plaVideo();
//        }
//    }
//
//    private void plaVideo(){
//        mp.reset();
//
//        try {
//            String url = vList.get(index).getUrl();
//            mp.setDataSource(url);
//            mp.prepare();
//            mp.start();
//            Log.e("MainActivity","------------"+url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
