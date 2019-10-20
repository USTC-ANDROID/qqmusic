package com.ustc.music.fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchAlbumRecyclerViewAdapter;
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


public class SearchAlbumFragment extends SearchFragment {
    private SearchAlbumRecyclerViewAdapter mSearchAlbumRecyclerViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchAlbumRecyclerViewAdapter = new SearchAlbumRecyclerViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mSearchAlbumRecyclerViewAdapter);
    }

    @Override
    protected void getData() {
        RequestUtil.get(getSearchUrl(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                final List<Map<String,String>> albumDataSource = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> stringObjectHashMap = new HashMap<>();
                        stringObjectHashMap.put("albumName", jsonObject.getString("albumName"));
                        stringObjectHashMap.put("albumPic", jsonObject.getString("albumPic"));
                        stringObjectHashMap.put("singerName", ((JSONObject)jsonObject.getJSONArray("singer_list").get(0)).getString("name"));
                        albumDataSource.add(stringObjectHashMap);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchAlbumRecyclerViewAdapter.addAllData(albumDataSource);
                            mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void setRefresh() {
        mSearchAlbumRecyclerViewAdapter.clearData();
        pageNo = 1;
    }

    @Override
    protected int getSearchRecyclerViewId() {
        return R.id.searchAlbumRecyclerView;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_album;
    }

    @Override
    protected int getSearchType() {
        return 8;
    }
}
