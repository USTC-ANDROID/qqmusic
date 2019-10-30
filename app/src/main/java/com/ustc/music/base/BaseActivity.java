package com.ustc.music.base;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ustc.music.R;
import com.ustc.music.entity.Music;
import com.ustc.music.service.SimpleService;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.StatusBarUtils;
import com.ustc.music.util.TranslucentStatusUtil;
import com.ustc.music.view.BottomTabsLayout;

import java.util.List;
import java.util.Map;

/**
 * 在调用onCreate方法的时候必须要调用父类的onCreate
 */
public abstract class BaseActivity extends AppCompatActivity {

    public SimpleService playService;
    public BottomTabsLayout bottomTabsLayout;
    private ImageView musicImage;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService = (SimpleService)((SimpleService.PlayBinder) service).getService();

            playService.setOnMusicEventListener(mMusicEventListener);
            playService.context = BaseActivity.this;
            initBottomTabsLayout();
            serviceInited();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

    protected void serviceInited() {

    }



    protected void onCreate(Bundle savedInstanceState, int resourceID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(resourceID);
//        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);


        bottomTabsLayout = findViewById(R.id.bottom_layout);
        musicImage = bottomTabsLayout.findViewById(R.id.music_img);
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        musicImage.startAnimation(operatingAnim);

        initView();
        initData();
        initAdapter();
        Intent intent = new Intent(this, SimpleService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        LinearLayout titleBar = findViewById(R.id.ll_title_bar);
        if(titleBar == null) return;
        TranslucentStatusUtil.initState(this, titleBar);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("test","onResume");
        initBottomTabsLayout();
    }

    private void initBottomTabsLayout() {
        if(bottomTabsLayout != null && playService != null && playService.getNowMusic() != null) {
            Music nowMusic = playService.getNowMusic();
            bottomTabsLayout.refershMusic(nowMusic.getAvatar(), nowMusic.getTitle());

        }
    }
    /**
     * 音乐播放服务回调接口的实现类
     */
    private SimpleService.OnMusicEventListener mMusicEventListener =
            new SimpleService.OnMusicEventListener() {
                @Override
                public void onPublish(int progress) {
                    BaseActivity.this.onPublish(progress);
                }

                @Override
                public void onChange(Music music) {
//                    BaseActivity.this.onChange(music);
                    Log.v("playcallback", "baseactivity  onChange");
                    bottomTabsLayout.refershMusic(music.getAvatar(), music.getTitle());
//                    bottomTabsLayout.changePlayerStatus();
                }
            };

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initAdapter();

    /**
     * 更新进度
     * @param progress 进度
     */
    public void onPublish(int progress) {

    }
    /**
     * 切换歌曲
     * @param music 歌曲在list中的位置
     */
    public void onChange(Music music) {
    }

    public boolean playStatus() {
        return playService.getStatus();
    }

    public boolean hasPlay() {
        return playService.getNowMusic() == null ? false : true;
    }

    public void stop() {
        playService.stop();
    }

    public void start() {
        playService.start();
    }

    public List<Music> getList() {
        return playService.getMusics();
    }

    public void playByMid(String mid) {
        playService.playByMid(mid);
    }

    public void remove(String mid) {
        playService.remove(mid);
    }
}
