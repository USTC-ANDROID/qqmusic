package com.ustc.music.fragment;

import com.ustc.music.R;

public class SearchSongListFragment extends SearchFragment {

    @Override
    protected void getData() {

    }

    @Override
    protected void setRefresh() {

    }

    @Override
    protected int getSearchRecyclerViewId() {
        return R.id.searchSongListRecyclerView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_song_list;
    }

    @Override
    protected int getSearchType() {
        return 0;
    }
}
