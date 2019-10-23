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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ustc.music.R;
import com.ustc.music.entity.Music;
import com.ustc.music.service.SimpleService;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.StatusBarUtils;
import com.ustc.music.view.BottomTabsLayout;

/**
 * 在调用onCreate方法的时候必须要调用父类的onCreate
 */
public abstract class BaseActivity extends Activity {

    protected SimpleService playService;
    protected BottomTabsLayout bottomTabsLayout;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            playService = (SimpleService)((SimpleService.PlayBinder) service).getService();

            playService.setOnMusicEventListener(mMusicEventListener);
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
        initView();
        initData();
        initAdapter();
        Intent intent = new Intent(this, SimpleService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);


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
                public void onChange(int position) {
                    BaseActivity.this.onChange(position);
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
     * @param position 歌曲在list中的位置
     */
    public void onChange(int position) {

    }
}