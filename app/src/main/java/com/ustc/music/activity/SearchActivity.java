package com.ustc.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchFragmentPagerAdapter;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.fragment.SearchFragment;

public class SearchActivity extends BaseActivity {

    private SearchView searchView;
    private ImageView goback;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchFragmentPagerAdapter searchFragmentPagerAdapter;

    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;
    private TabLayout.Tab four;

    private String searchKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, R.layout.activity_search);
        initView();
    }

    protected void initView() {
        searchView = findViewById(R.id.qq_search_view);
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String queryStr = s.trim();
                if (queryStr.isEmpty()) {
                    return false;
                } else {
                    searchKeyWord = queryStr;
                    for (int i = 0; i != searchFragmentPagerAdapter.getCount(); ++i) {
                        final SearchFragment item = searchFragmentPagerAdapter.getItem(i);
                        item.setSearchKeyword(searchKeyWord);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                item.onRefresh();
                            }
                        }).start();
                    }
                    return true;
                }
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        goback = findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= findViewById(R.id.viewPager);
        searchFragmentPagerAdapter = new SearchFragmentPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(searchFragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(searchFragmentPagerAdapter.getCount());
        //将TabLayout与ViewPager绑定在一起
        mTabLayout = findViewById(R.id.tabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        four = mTabLayout.getTabAt(3);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initAdapter() {

    }

}
