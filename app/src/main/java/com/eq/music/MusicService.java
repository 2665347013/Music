package com.eq.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

public class MusicService extends Service {
    private MediaPlayer player;
    private Timer timer;
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
    }

    //添加计时器，用于设置音乐播放器中的播放进度条
    public void addTimer(){
        if (timer ==null){
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (player==null) return;
                    int duration = player.getDuration();//获取歌曲的总长度
                    //获取播放进度
                    int currentDuration = player.getCurrentPosition();
                    //创建消息对象
                    Message msg = MusicDetailActivity.handler.obtainMessage();
                    //将音乐的总时长、播放时长封装到消息中去
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration",duration);
                    bundle.putInt("currentDuration",currentDuration);
                    msg.setData(bundle);
                    //将消息发送到主线程的消息队列中去
                    MusicDetailActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5ms，第一次执行task任务，以后没500ms执行一次
            timer.schedule(task,5,500);
        }
    }


    class  MusicControl extends Binder{
        public void play(){

            try {
                player.reset();//重置播放器
                //加载多媒体音乐文件
                player = MediaPlayer.create(getApplicationContext(),R.raw.music);
                player.start();//播放音乐
                addTimer();//添加定时器
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        public void pausePlay(){
            player.pause();
        }
        public void continuePlay(){
            player.start();
        }
        public void seekTo(int progress){
            player.seekTo(progress);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return new MusicControl();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player ==null) return;
        if (player.isLooping()) player.stop();
        player.release();
        player = null;
    }
}
