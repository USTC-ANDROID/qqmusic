package com.ustc.music.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ustc.music.fragment.SearchAlbumFragment;
import com.ustc.music.fragment.SearchSongFragment;
import com.ustc.music.fragment.SearchSongListFragment;
import com.ustc.music.fragment.SearchVideoFragment;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"歌曲", "视频", "专辑","歌单"};

    public SearchFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new SearchAlbumFragment();
        } else if (position == 2) {
            return new SearchSongFragment();
        }else if (position==3){
            return new SearchSongListFragment();
        }
        return new SearchVideoFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
