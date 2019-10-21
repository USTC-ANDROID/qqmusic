package com.ustc.music.fragment;

import android.os.Bundle;
import android.view.View;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchVideoRecyclerViewAdapter;
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

public class SearchVideoFragment extends SearchFragment {

    private SearchVideoRecyclerViewAdapter mSearchVideoRecyclerViewAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchVideoRecyclerViewAdapter = new SearchVideoRecyclerViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mSearchVideoRecyclerViewAdapter);
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
                final List<Map<String,String>> videoDataSource = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> stringObjectHashMap = new HashMap<>();
                        stringObjectHashMap.put("vedioName", jsonObject.getString("mv_name"));
                        stringObjectHashMap.put("vedioPic", jsonObject.getString("mv_pic_url"));
                        stringObjectHashMap.put("singerName", ((JSONObject)jsonObject.getJSONArray("singer_list").get(0)).getString("name"));
                        stringObjectHashMap.put("playCnt", jsonObject.getString("play_count"));
                        videoDataSource.add(stringObjectHashMap);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchVideoRecyclerViewAdapter.addAllData(videoDataSource);
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
        mSearchVideoRecyclerViewAdapter.clearData();
        pageNo = 1;
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
        return 12;
    }
}
