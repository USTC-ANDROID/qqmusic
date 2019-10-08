package com.ustc.music.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ustc.music.fragment.SearchAlbumFragment;
import com.ustc.music.fragment.SearchSongFragment;
import com.ustc.music.fragment.SearchSongListFragment;
import com.ustc.music.fragment.SearchVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"歌曲", "视频", "专辑","歌单"};
    private List<Fragment> fragments = new ArrayList<>();

    public SearchFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new SearchSongFragment());
        fragments.add(new SearchVideoFragment());
        fragments.add(new SearchAlbumFragment());
        fragments.add(new SearchSongListFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
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
