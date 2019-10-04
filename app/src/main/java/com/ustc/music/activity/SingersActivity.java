package com.ustc.music.activity;

import android.app.Activity;
import android.os.Bundle;

import com.ustc.music.R;
import com.ustc.music.util.StatusBarUtils;
import com.ustc.music.view.HorizontalListView;

public class SingersActivity extends Activity {

    private String[] addrs = new String[] {"全部", "内地", "港台", "欧美", "日本", "韩国", "其他"};
    private String[] sexs = new String[] {"男", "女", "组合"};
    private HorizontalListView addrListView;
    private HorizontalListView sexListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singers);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
    }
}
