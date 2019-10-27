package com.ustc.music.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(setContentView(), container, false);
        findViewById(inflate);
        Log.v("basefragment", "oncreateView");
        return inflate;
    }

    /*
     * 初始化布局：
     *      findViewById() 或者 ButterKnife.bind(this, view);
     * @param rootView 布局UI
     */
    protected abstract void findViewById(View rootView);
    /*
     * 记载布局文件：
     *      R.layout.xxx
     */
    protected abstract int setContentView();
}
