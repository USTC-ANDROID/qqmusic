package com.ustc.music.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ustc.music.base.BaseActivity;
import com.ustc.music.fragment.SearchAlbumFragment;
import com.ustc.music.fragment.SearchFragment;
import com.ustc.music.fragment.SearchSongFragment;
import com.ustc.music.fragment.SearchSongListFragment;
import com.ustc.music.fragment.SearchVideoFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] mTitles = new String[]{"歌曲", "视频", "专辑","歌单"};
    private List<SearchFragment> fragments = new ArrayList<>();
    private SearchFragment mCurrentFragment;
    private BaseActivity baseActivity;

    public SearchFragmentPagerAdapter(FragmentManager fm,BaseActivity baseActivity) {
        super(fm);
        this.baseActivity = baseActivity;
        SearchSongFragment e = new SearchSongFragment();
        e.setBaseActivity(baseActivity);
        fragments.add(e);
        fragments.add(new SearchVideoFragment());
        fragments.add(new SearchAlbumFragment());
        fragments.add(new SearchSongListFragment());
    }

    @Override
    public SearchFragment getItem(int position) {
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


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (SearchFragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public SearchFragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
