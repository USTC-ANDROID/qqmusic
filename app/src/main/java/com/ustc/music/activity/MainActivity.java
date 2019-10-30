package com.ustc.music.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.fragment.FragmentAction;
import com.ustc.music.fragment.FragmentHome;
import com.ustc.music.fragment.FragmentMe;
import com.ustc.music.fragment.FragmentRecomment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private TabLayout mTabLayout;
    private String[] tabsNames = new String[] {"音乐馆", "推荐", "动态", "我的"};
    private int[] tabsIcon = {R.drawable.tab1_disable, R.drawable.tab2_disable, R.drawable.tab3_disable, R.drawable.tab4_disable};
    private int[] tabsActiveIcon = {R.drawable.tab1_active, R.drawable.tab2_active, R.drawable.tab3_active, R.drawable.tab4_active};
    private List<View> viewList;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("哈哈哈");
        onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void initView() {
        Log.v("mainactivity1", "initView");
        mTabLayout = (TabLayout) findViewById(R.id.bottom_tab_layout);
        viewPager = findViewById(R.id.pager);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.v("tab", tab.getPosition() + "");
//                viewPager.scroll
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mTabLayout.addTab(mTabLayout.newTab().setIcon(tabsIcon[0]).setText(tabsNames[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(tabsIcon[1]).setText(tabsNames[1]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(tabsIcon[2]).setText(tabsNames[2]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(tabsIcon[3]).setText(tabsNames[3]));

        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHome());
        adapter.addFragment(new FragmentRecomment());
        adapter.addFragment(new FragmentAction());
        adapter.addFragment(new FragmentMe());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        //要设置viewpager的limit为2，因为滑动到fragment3的时候，fragment3的左侧有2个页面了
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
//                mTabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageSelected(int i) {
                mTabLayout.getTabAt(i).select();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initAdapter() {

    }

    class MyAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment) {
            fragments.add(fragment);
        }
    }



}
