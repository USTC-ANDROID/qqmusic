package com.ustc.music.fragment;

import com.ustc.music.R;

public class SearchVideoFragment extends SearchFragment {


    @Override
    protected void getData() {

    }

    @Override
    protected void setRefresh() {

    }

    @Override
    protected int getSearchRecyclerViewId() {
        return R.id.searchVideoRecyclerView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_video;
    }

    @Override
    protected int getSearchType() {
        return 0;
    }
}
