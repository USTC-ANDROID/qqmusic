package com.ustc.music.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ustc.music.entity.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SimpleService extends Service implements MediaPlayer.OnCompletionListener {


    private MediaPlayer mPlayer ;


    private volatile boolean playing = false;

    private Music nowMusic;

    private List<Music> musics = Collections.<Music>synchronizedList(new ArrayList<Music>());

    private PlayBinder mBinder = new PlayBinder();

    private OnMusicEventListener mListener;

    private ExecutorService mProgressUpdatedListener = Executors.newSingleThreadExecutor();

    @Override
    public void onCompletion(MediaPlayer mp) {
        play();
    }

    public class PlayBinder extends Binder {

        public Service getService() {
            return SimpleService.this;
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();

        mPlayer.setOnCompletionListener(this);
        mProgressUpdatedListener.execute(mPublishProgressRunnable);
    }
    public void seek(int msec) {
        if(!getStatus()) return;
        mPlayer.seekTo(msec);
    }

    /**
     * 更新进度的线程
     */
    private Runnable mPublishProgressRunnable = new Runnable() {
        @Override
        public void run() {
            for(;;) {
                if(mPlayer != null && mPlayer.isPlaying() &&
                        mListener != null) {
                    mListener.onPublish(mPlayer.getCurrentPosition());
//                    mPlayer.
                }

                SystemClock.sleep(200);
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }//普通服务的不同之处，onBind()方法不在打酱油，而是会返回一个实例


    public boolean getStatus() {
        return playing;
    }

    public void add(Music music) {
        musics.add(0, music);
    }

    public void stop() {
        this.playing = false;
    }

    public void playNow(String url) {
        Music music = new Music();
        music.setMusicSource(url);
        musics.add(music);
    }

    public Music getNowMusic() {
        return nowMusic;
    }

    public void play() {
        this.playing = true;
        Music poll = musics.remove(0);
        nowMusic = poll;
        musics.add(poll);
        mPlayer.reset();
        try {
            mPlayer.setDataSource(poll.getMusicSource());
            mPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }
    /**
     * 设置回调
     * @param l
     */
    public void setOnMusicEventListener(OnMusicEventListener l) {
        mListener = l;
    }

    /**
     * 音乐播放回调接口
     * @author qibin
     */
    public interface OnMusicEventListener {
        public void onPublish(int percent);
        public void onChange(int position);
    }

    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    /**
     * 服务销毁时，释放各种控件
     */
    private void release() {
        if(!mProgressUpdatedListener.isShutdown()) mProgressUpdatedListener.shutdownNow();
        mProgressUpdatedListener = null;

        if(mPlayer != null) mPlayer.release();
        mPlayer = null;
    }


}