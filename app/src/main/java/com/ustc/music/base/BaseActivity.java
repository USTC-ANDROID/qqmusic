package com.ustc.music.base;

import android.app.Activity;
import android.os.Bundle;

import com.ustc.music.R;
import com.ustc.music.util.StatusBarUtils;

/**
 * 在调用onCreate方法的时候必须要调用父类的onCreate
 */
public abstract class BaseActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState, int resourceID) {
        super.onCreate(savedInstanceState);
        setContentView(resourceID);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        initData();
        initAdapter();
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void initAdapter();
}
