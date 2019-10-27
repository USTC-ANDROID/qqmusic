package com.ustc.music.view;

import android.os.Bundle;

public abstract class LazyBaseFragment extends BaseFragment {

    /**
     * 是否初始化布局
     */
    protected boolean isViewInitiated;

    /**
     * 当前界面是否可见
     */
    protected boolean isVisibleToUser;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        isCanLoadData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 是否对用户可见
        this.isVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            isCanLoadData();
        }
    }

    /*
     * 执行数据加载： 条件是view初始化完成并且对用户可见
     */
    private void isCanLoadData() {
        if (isViewInitiated && isVisibleToUser) {
            lazyLoad();
            // 加载过数据后，将isViewInitiated和isVisibleToUser设置成false，防止重复加载数据
            isViewInitiated = false;
            isVisibleToUser = false;
        }
    }
    protected abstract void lazyLoad();


}
