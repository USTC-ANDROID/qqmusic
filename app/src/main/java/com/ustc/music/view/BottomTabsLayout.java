package com.ustc.music.view;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.activity.PlayActivity;
import com.ustc.music.base.BaseActivity;

public class BottomTabsLayout extends LinearLayout implements View.OnClickListener {


    private ImageView musicImg;
    private TextView musicTitle;
    private ImageView musicPlayer;
    private ImageView musicList;

    public BottomTabsLayout(Context context) {
        super(context);
        init();
    }

    public BottomTabsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomTabsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BottomTabsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.bottomlayout, this);
        this.musicList = findViewById(R.id.music_list);
        this.musicPlayer = findViewById(R.id.music_player);
        this.musicTitle = findViewById(R.id.music_title);
        this.musicImg = findViewById(R.id.music_img);
        refershMusic("", "QQ音乐");
        this.setOnClickListener(this);
        this.musicPlayer.setOnClickListener(this);
//        ((BaseActivity)getContext()).initBottomTabs();
        Log.v("test", "测试");
    }


    public void refershMusic(String url, String title) {
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.newmusic)
                .error(R.drawable.newmusic)
                .into(musicImg);
        musicTitle.setText(title);

        this.musicPlayer.setImageResource(R.drawable.player_to_stop);
    }

    public void changePlayerStatus() {
        BaseActivity activity = (BaseActivity) getContext();
        if(activity.playStatus()) {
            activity.stop();
            this.musicPlayer.setImageResource(R.drawable.player_to_start);
        } else {
            activity.start();
            this.musicPlayer.setImageResource(R.drawable.player_to_stop);
        }
    }

//    public void changeToPlay


    @Override
    public void onClick(View v) {
        if(!((BaseActivity)getContext()).hasPlay()) {
            SmileToast.makeSmileToast(getContext(),
                    "请添加歌曲到播放列表", SmileToast.LENGTH_LONG).show();
            return;
        }
        if(v == this) {
            Intent intent = new Intent(getContext(), PlayActivity.class);
            getContext().startActivity(intent);
        } else if(v == this.musicPlayer) {
            changePlayerStatus();
        }
    }




}
