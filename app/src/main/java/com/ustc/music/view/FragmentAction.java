package com.ustc.music.view;

import android.view.View;

import com.ustc.music.R;

public class FragmentAction extends LazyBaseFragment {



    @Override
    protected void findViewById(View rootView) {

    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_menu_2;
    }

    @Override
    protected void lazyLoad() {
//        这里进行网络请求

    }
}