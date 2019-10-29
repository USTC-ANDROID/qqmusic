package com.ustc.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchSongRecyclerViewAdapter;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class SearchSongFragment extends SearchFragment implements SearchSongRecyclerViewAdapter.SearchSongClickListener {

    private SearchSongRecyclerViewAdapter mSearchSongRecyclerViewAdapter;
    //    final List<Pair<String, String>> songSingerDataSource = new ArrayList<>();
    final List<Map<String, String>> songSingerDataSource = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchSongRecyclerViewAdapter = new SearchSongRecyclerViewAdapter(getActivity(), songSingerDataSource, this);
        mPullLoadMoreRecyclerView.setAdapter(mSearchSongRecyclerViewAdapter);
    }

    protected void getData() {
        RequestUtil.get(getSearchUrl(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String songName = jsonObject.getString("songname");
                        String singerName = ((JSONObject)jsonObject.getJSONArray("singer").get(0)).getString("name");
                        HashMap<String, String> stringStringHashMap = new HashMap<>();
                        stringStringHashMap.put("songName", songName);
                        stringStringHashMap.put("singerName", singerName);
                        stringStringHashMap.put("songmid", jsonObject.getString("songmid"));
                        stringStringHashMap.put("albummid", jsonObject.getString("albummid"));
                        songSingerDataSource.add(stringStringHashMap);
                    }
                    getActivity().runOnUiThread(() -> {
                        mSearchSongRecyclerViewAdapter.addAllData(songSingerDataSource);
                        mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void clearData() {
        mSearchSongRecyclerViewAdapter.clearData();
        mSearchSongRecyclerViewAdapter.notifyDataSetChanged();
    }

    protected void setRefresh() {
        mSearchSongRecyclerViewAdapter.clearData();
        pageNo = 1;
    }

    @Override
    protected int getSearchRecyclerViewId() {
        return R.id.searchSongRecyclerView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_song;
    }

    @Override
    protected int getSearchType() {
        return 0;
    }

    @Override
    public void clickListener(View v) {
        Toast.makeText(
                getActivity(),
                "listview的内部的按钮被点击了！，位置是-->" + v.getTag() + ",内容是-->"
                        + songSingerDataSource.get((Integer) v.getTag()),
                Toast.LENGTH_SHORT).show();
    }
}
