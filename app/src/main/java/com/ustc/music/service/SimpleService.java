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

import com.alibaba.fastjson.JSONObject;
import com.ustc.music.activity.RecommentListActivity;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.core.MiGuMusicSource;
import com.ustc.music.core.MusicRequestCallBack;
import com.ustc.music.entity.Music;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.view.SmileToast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SimpleService extends Service implements MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer ;
    private Music nowMusic;
    private CircleLinkList musics = null;
    Map<String, String> map = new HashMap<>();
    private PlayBinder mBinder = new PlayBinder();
    private OnMusicEventListener mListener;
    private ExecutorService mProgressUpdatedListener = Executors.newSingleThreadExecutor();
    public BaseActivity context;

    public void start() {
        mPlayer.start();
    }

    public void stop() { this.mPlayer.pause(); }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
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

        map.put("Referer", "https://y.qq.com/");
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

    /**
     * 当前是否播放状态
     * @return
     */
    public boolean getStatus() {
        return mPlayer.isPlaying();
    }

    /**
     * 加入歌单
     * @param music
     */
    public void add(Music music) {
        if (musics == null) musics = new CircleLinkList(new Node(music));
        else musics.add(new Node(music));
        if (nowMusic == null || !mPlayer.isPlaying()) play(music);
    }

    public void playNow(Music music) {
        if (musics == null) musics = new CircleLinkList(new Node(music));
        else musics.add(new Node(music));
        play(music);
    }

    public void playByMid(String mid) {
        Music exist = musics.exist(mid);
        if(exist == null) return;
        play(exist);
    }

    public Music getNowMusic() {
        return nowMusic;
    }

    private void play(Music music) {


        RequestUtil.get(DataUrl.playMusicStep1.replace("{1}", music.getMid()), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.v("musicsource",music.getMid() + " --  " + string);
                final String musicSource = JSONObject.parseObject(string)
                        .getJSONObject("req_0")
                        .getJSONObject("data")
                        .getJSONArray("midurlinfo")
                        .getJSONObject(0).getString("purl");
                if("".equals(musicSource)) {
                    MiGuMusicSource.loadMusicSourceFromMiGu(music.getTitle(), music.getAuthor(), new MusicRequestCallBack() {
                        @Override
                        public void call(final String source) {
                            context.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SmileToast.makeSmileToast(context,
                                            "对不起，QQ音乐源不能播放，正为你切换到咪咕音乐源",
                                            SmileToast.LENGTH_LONG).show();
                                    music.setMusicSource(source);
                                    setMusicPlay(music);
                                }
                            });
                        }
                    });
                } else {
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String source = musicSource;
                            if(source.contains("qq.com")) {
                                source = source.substring(source.indexOf("qq.com/C") + 7);
                            }
                            music.setMusicSource(DataUrl.playMusicStep2.replace("{1}", source));
                            setMusicPlay(music);
                        }
                    });
                }
            }
        });

    }

    private void setMusicPlay(Music music) {
        mPlayer.reset();
        nowMusic = music;
//        Log.v("simpleservice", music.getMusicSource());
        try {
            mPlayer.setDataSource(music.getMusicSource());
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.v("simpleservice", "测试");
                    mp.start();
                }
            });
            mListener.onChange(music);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void playNext() {
        Log.v("playcallback", "play");
        play(musics.next().value);
    }

    public void playPrev() {
        Log.v("playcallback", "play");
        play(musics.prev().value);
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
        public void onChange(Music position);
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

    public List<Music> getMusics() {
        return musics.toList();
    }

    public void remove(String mid) {
        musics.remove(mid);
    }



    class CircleLinkList {



        private Node current;

        public CircleLinkList(Node current) {
            this.current = current;
            current.prev = current;
            current.next = current;
        }

        public Music exist(String mid) {
            if(current == null) return null;
            Node temp = current;
            do {
                if (temp.value.getMid().equals(mid)) return temp.value;
                temp = temp.next;
            } while ((temp != current));
            return null;
        }

        public boolean add(Node node) {
            if(exist(node.value.getMid()) != null) return false;
//        node.next = current;
//        node.prev = current.prev;
//        current.prev.next = node;
//        current.prev = node;
            node.next = current.next;
            current.next.prev = node;
            current.next = node;
            node.prev = current;
            current = current.next;
            return true;
        }



        public Node next() {
            current = current.next;
            return current;
        }

        public Node prev() {
            current = current.prev;
            return current;
        }
        public void remove(Node node) {
            Node temp = current;
            while(temp.next != current) {
                if (temp.value.getTitle().equals(node.value.getTitle())) {
                    temp = node;
                    temp.prev.next = temp.next;
                    return;
                }
                temp = temp.next;
            }
        }
        public void remove(String mid) {
            Node temp = current;
            do {
                if (temp.value.getMid().equals(mid)) {
                    if(temp.next == temp) {
                        musics = null;
                        mPlayer.stop();
                        nowMusic = null;

                    } else {
                        temp.prev.next = temp.next;
                        temp.next.prev = temp.prev;
                        if(nowMusic.getMid().equals(mid)) playNext();
                    }
                    return;
                }
                temp = temp.next;
            } while (temp != current);
        }

        public List<Music> toList() {
            Node temp = current;
            List<Music> list = new ArrayList<>();
            do {

                list.add(temp.value);
                temp = temp.next;
            } while ((temp != current));
            return list;
        }
    }


}

class Node {
    protected Music value;
    protected Node prev;
    protected Node next;

    public Node(Music value) {
        this.value = value;
    }
}