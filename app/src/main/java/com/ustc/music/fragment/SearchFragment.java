package com.ustc.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ustc.music.R;
import com.ustc.music.url.DataUrl;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

public abstract class SearchFragment extends Fragment implements PullLoadMoreRecyclerView.PullLoadMoreListener {
    protected PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
    protected RecyclerView mRecyclerView;
    protected String searchKeyword;
    protected int pageNo = 1;
    protected boolean needToRefresh = false;

    protected abstract void getData();

    protected abstract void setRefresh();

    protected abstract int getSearchRecyclerViewId();

    protected abstract int getLayoutId();

    protected abstract int getSearchType();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getLayoutId(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPullLoadMoreRecyclerView = view.findViewById(getSearchRecyclerViewId());
        //获取mRecyclerView对象
        mRecyclerView = mPullLoadMoreRecyclerView.getRecyclerView();
        //代码设置scrollbar无效？未解决！
        mRecyclerView.setVerticalScrollBarEnabled(true);
        //显示下拉刷新
        mPullLoadMoreRecyclerView.setRefreshing(false);
        //设置上拉刷新文字
        mPullLoadMoreRecyclerView.setFooterViewText("正在加载更多");
        mPullLoadMoreRecyclerView.setLinearLayout();

        mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);

    }

    @Override
    public void onRefresh() {
//        if (needToRefresh) {
            Log.e("wxl", "onRefresh");
            setRefresh();
            getData();
            needToRefresh = false;
//        }
    }

    @Override
    public void onLoadMore() {
        Log.e("wxl", "onLoadMore");
        ++pageNo;
        getData();
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        if (!searchKeyword.equals(this.searchKeyword)) {
            this.searchKeyword = searchKeyword;
            this.needToRefresh = true;
        }
    }

    protected String getSearchUrl() {
        return DataUrl.searchUrl + "t="+getSearchType()+"&" + "key=" + searchKeyword + "&pageNo=" + pageNo;
    }
}
