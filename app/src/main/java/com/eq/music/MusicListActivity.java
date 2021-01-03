package com.eq.music;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MusicListActivity extends Activity implements MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener,View.OnClickListener {
    private GridView gridView;
    private String[] titleArr;
    private String[] countArr;
    private int[] iconArr;
    private RadioButton rb_mllb,rb_grzx;
    ArrayList<Music> musicList;
    ArrayList<Video> videoList;
    GetMusicUtil util;
    GetVideoUtil videoUtil;

    private MyViewPager mLoopPager;
    private LooperPagerAdapter mLooperPagerAdapter;

    private static List<Integer> sPics = new ArrayList<>();

    static {
        sPics.add(R.mipmap.m1);
        sPics.add(R.mipmap.m2);
        sPics.add(R.mipmap.m3);
    }
    private Handler mHandler;
    private boolean mIsTouch = false;
    private LinearLayout mPointContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        gridView= (GridView) findViewById(R.id.gridview);
        rb_mllb = (RadioButton) findViewById(R.id.rb_mllb);
        rb_grzx = (RadioButton) findViewById(R.id.rb_grzx);
        rb_mllb.setOnClickListener(this);
        rb_grzx.setOnClickListener(this);
        mHandler = new Handler();
        mLoopPager = (MyViewPager) findViewById(R.id.looper_pager);
        mPointContainer = (LinearLayout) findViewById(R.id.points_container);
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        mLoopPager.setAdapter(mLooperPagerAdapter);
        mLoopPager.addOnPageChangeListener(this);
        mLoopPager.setOnViewPagerTouchListener(this);
        //加点
        insertPoint();
        mLoopPager.setCurrentItem(mLooperPagerAdapter.getDataRealSize() * 100,false);
        setData();
        gridView.setAdapter(new MyGridviewAdapter(this,iconArr,titleArr,countArr));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(MusicListActivity.this,MusicListDetailActivity.class);
                        intent.putExtra("music",musicList);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MusicListActivity.this,RegisterActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent8 = new Intent(MusicListActivity.this,SelectPage.class);
                        startActivity(intent8);
                        break;
                }
            }
        });
        initEvent();
    }

    private void initEvent() {
        if (mLooperPagerAdapter != null){
            mLooperPagerAdapter.setpagerItemClickListener(new LooperPagerAdapter.OnpagerItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    switch (position){
                        case 0:
                            Intent intent = new Intent(MusicListActivity.this,MusicDetailActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(MusicListActivity.this,VideoViewActivity.class);
                            intent1.putExtra("video",videoList);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(MusicListActivity.this,RegisterActivity.class);
                            startActivity(intent2);
                            startActivity(intent2);
                            break;
                    }
                }
            });
        }
    }

    private void setData() {
        //获取内存接收者
        ContentResolver resolver= getContentResolver();
        ContentResolver contentResolver = getContentResolver();

        util = new GetMusicUtil();
        videoUtil = new GetVideoUtil();

        musicList = util.getMusic(resolver);
        videoList = videoUtil.getVideo(contentResolver);

        iconArr= new int[9];
        iconArr[0]=R.mipmap.ic_launcher;
        iconArr[1]=R.mipmap.ic_launcher;
        iconArr[2]=R.mipmap.ic_launcher;

        titleArr= new String[]{"我的音乐","我的最爱","返回首页"};

        countArr= new String[]{musicList.size()+"",videoList.size()+"","0",};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rb_mllb:
                Intent intent = new Intent(this,MusicListActivity.class);
                startActivity(intent);
                break;
            case R.id.rb_grzx:
                Intent intent2 = new Intent(this,SelectPage.class);
                startActivity(intent2);
                break;
        }
    }
    private void insertPoint() {
        for (int i = 0; i < sPics.size(); i++){
            View point = new View(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
            point.setBackground(getResources().getDrawable(R.drawable.shape_point_normal));
            layoutParams.leftMargin = 20;
            point.setLayoutParams(layoutParams);
            mPointContainer.addView(point);
        }
    }
    @Override
    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        mHandler.post(mLooperTask);
    }
    @Override
    public void onDetachedFromWindow(){
        super.onDetachedFromWindow();

        mHandler.removeCallbacks(mLooperTask);
    }
    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if (!mIsTouch){
                int currentItem = mLoopPager.getCurrentItem();
                mLoopPager.setCurrentItem(++currentItem,true);
            }
            mHandler.postDelayed(this,3000);
        }
    };

    @Override
    public void onPagerTouch(boolean isTouch) {
        mIsTouch = isTouch;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //停下来选中的位置
        int realPosition;
        if (mLooperPagerAdapter.getDataRealSize() != 0){
            realPosition = position % mLooperPagerAdapter.getDataRealSize();
        }else {
            realPosition = 0;
        }
        setSelectPoint(realPosition);
    }

    private void setSelectPoint(int realPosition) {
        for (int i =0; i < mPointContainer.getChildCount(); i++){
            View point = mPointContainer.getChildAt(i);
            if (i != realPosition){
                point.setBackgroundResource(R.drawable.shape_point_normal);
            }else {
                point.setBackgroundResource(R.drawable.shape_point_selected);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
