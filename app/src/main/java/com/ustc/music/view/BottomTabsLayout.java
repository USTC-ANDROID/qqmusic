package com.ustc.music.view;

import android.annotation.SuppressLint;
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

    private LinearLayout tabs;
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
        LayoutInflater.from(getContext()).inflate(R.layout.bottom_tab_layout, this);
        this.musicList = findViewById(R.id.music_list);
        this.musicPlayer = findViewById(R.id.music_player);
        this.musicTitle = findViewById(R.id.music_title);
        this.tabs = findViewById(R.id.tabs);
        this.musicImg = findViewById(R.id.music_img);
        refershMusic("", "QQ音乐");
        this.setOnClickListener(this);
//        ((BaseActivity)getContext()).initBottomTabs();
        Log.v("test", "测试");
    }

    public void setTabsHidden() {
        this.tabs.setVisibility(INVISIBLE);
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height /= 2;
        this.setLayoutParams(layoutParams);
    }

    public void setBottomHidden() {
        this.setVisibility(INVISIBLE);
    }

    public void refershMusic(String url, String title) {
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.newmusic)
                .error(R.drawable.newmusic)
                .into(musicImg);
        musicTitle.setText(title);
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        getContext().startActivity(intent);
    }




}
