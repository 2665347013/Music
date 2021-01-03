package com.eq.music;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MusicDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_cover; //封面图片
    private static SeekBar sb; //进度条控件
    private static TextView tv_progress, tv_total; //当前播放时长，总时长 控件
    private Button btn_play,btn_pause,btn_continue,btn_exit; //四个控件按钮

    private ObjectAnimator animator; //动画控件

    private MusicService.MusicControl musicControl; // 音乐控制 服务中的Binder 对象
    private MyServiceConn myServiceConn; //连接对象

    private Intent intent; //全局的意图

    private boolean isUnbind = false; //记录服务是否被解绑

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        initView();
    }

    private void initView() {
        iv_cover = (ImageView) findViewById(R.id.iv_cover);
        animator = ObjectAnimator.ofFloat(iv_cover,"rotation",0f,360.0f);
        animator.setDuration(10000); //旋转一周用时 10s
        animator.setInterpolator(new LinearInterpolator()); //匀速运动
        animator.setRepeatCount(-1); // -1 表示动画无限循
        intent = new Intent(this,MusicService.class); //打开服务
        myServiceConn = new MyServiceConn(); //服务连接对象
        bindService(intent,myServiceConn,BIND_AUTO_CREATE); //绑定服务

        sb = (SeekBar) findViewById(R.id.sb);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            //进度条的进度变化时，调用这个方法
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress==seekBar.getMax()){ //滑动到最大值时，结束动画
                    animator.pause(); //停止 动画，此处需要api19，需要修改build.gradle
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            //滑动条停止滑动时的事件处理
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress(); //获取进度值
                musicControl.seekTo(progress);// 改变播放器的进度条
            }
        });
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_total = (TextView) findViewById(R.id.tv_total);

        btn_play = (Button) findViewById(R.id.btn_play);
        btn_pause = (Button) findViewById(R.id.btn_pause);
        btn_continue = (Button) findViewById(R.id.btn_continue);
        btn_exit = (Button) findViewById(R.id.btn_exit);

        btn_play.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                musicControl.play();
                animator.start();
                break;
            case R.id.btn_pause:
                musicControl.pausePlay();
                animator.pause();
                break;
            case R.id.btn_continue:
                musicControl.continuePlay();
                animator.start();
                break;
            case R.id.btn_exit:
                myUnbind();
                finish();
                break;
        }
    }
    //自定义解绑服务方法
    private void myUnbind(){
        if (!isUnbind){
            isUnbind = true;
            musicControl.pausePlay();
            unbindService(myServiceConn);
            stopService(intent);
        }
    }

    //创建消息处理对象
    public static Handler handler = new Handler(){
        //在主线程处理从子线程传来的消息
        @Override
        public void handleMessage(@NonNull Message msg){
            Bundle bundle = msg.getData(); //取出子线程传来的消息
            int duration = bundle.getInt("duration");
            int currentDuration = bundle.getInt("currentDuration");

            sb.setMax(duration);//设置seekBar的最大值
            sb.setProgress(currentDuration); //设置SeekBar的播放进度
            //计算歌曲的总时长信息
            int minute = duration / 1000 / 60; //分钟
            int second = duration /1000 % 60; //秒
            String strMinute = "";
            String strSecond = "";
            if (minute<10){
                strMinute = "0"+minute;
            }else {
                strMinute = minute + "";
            }
            if (second<10){
                strSecond = "0"+ second;
            }else {
                strSecond = second + "";
            }
            tv_total.setText(strMinute+":"+strSecond);
            //计算歌曲的总时长信息
            minute = currentDuration / 1000 / 60; //分钟
            second = currentDuration /1000 % 60; //秒
            if (minute<10){
                strMinute = "0"+minute;
            }else {
                strMinute = minute + "";
            }
            if (second<10){
                strSecond = "0"+ second;
            }else {
                strSecond = second + "";
            }
            tv_progress.setText(strMinute+":"+strSecond);
        }
    };

    class MyServiceConn implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MusicControl)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}

