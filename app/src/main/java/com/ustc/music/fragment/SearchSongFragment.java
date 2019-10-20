package com.ustc.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.View;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchSongRecyclerViewAdapter;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class SearchSongFragment extends SearchFragment {

    private SearchSongRecyclerViewAdapter mSearchSongRecyclerViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchSongRecyclerViewAdapter = new SearchSongRecyclerViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mSearchSongRecyclerViewAdapter);
    }

    protected void getData() {
        String searchSongUrl = DataUrl.searchUrl + "t=0&" + "key=" + searchKeyword + "&pageNo=" + pageNo;
        RequestUtil.get(searchSongUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                final List<Pair<String, String>> songSingerDataSource = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String songName = jsonObject.getString("songname");
                        String singerName = ((JSONObject)jsonObject.getJSONArray("singer").get(0)).getString("name");
                        Pair<String, String> songSingerPair = new Pair<>(songName, singerName);
                        songSingerDataSource.add(songSingerPair);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchSongRecyclerViewAdapter.addAllData(songSingerDataSource);
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
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
}
