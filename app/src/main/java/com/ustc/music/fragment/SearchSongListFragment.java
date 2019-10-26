package com.ustc.music.fragment;

import android.os.Bundle;
import android.view.View;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchSongListRecyclerViewAdapter;
import com.ustc.music.util.RequestUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SearchSongListFragment extends SearchFragment {

    private SearchSongListRecyclerViewAdapter mSearchSongListRecyclerViewAdapter;
    private static final DecimalFormat wanDecimalFormat = new DecimalFormat("0.0");


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchSongListRecyclerViewAdapter = new SearchSongListRecyclerViewAdapter(getActivity());
        mPullLoadMoreRecyclerView.setAdapter(mSearchSongListRecyclerViewAdapter);
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
                final List<Map<String,String>> songListDataSource = new ArrayList<>();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("data").getJSONArray("list");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        HashMap<String, String> stringObjectHashMap = new HashMap<>();
                        stringObjectHashMap.put("songListPic", jsonObject.getString("imgurl"));
                        stringObjectHashMap.put("songListName", replaceAscIIChar(jsonObject.getString("dissname")));
                        Integer listennum = Integer.parseInt(jsonObject.getString("listennum"));
                        String listennumStr;
                        if (listennum >= 10000) {
                            listennumStr = wanDecimalFormat.format(1.0 * listennum / 10000) + "万";
                        } else {
                            listennumStr = Integer.toString(listennum);
                        }
                        stringObjectHashMap.put("listenNum", listennumStr+"人播放");
                        stringObjectHashMap.put("songCnt", jsonObject.getString("song_count")+"首");
                        stringObjectHashMap.put("creatorName", replaceAscIIChar(jsonObject.getJSONObject("creator").getString("name")));
                        songListDataSource.add(stringObjectHashMap);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSearchSongListRecyclerViewAdapter.addAllData(songListDataSource);
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
        mSearchSongListRecyclerViewAdapter.clearData();
        pageNo = 1;
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
        return 2;
    }

    private String replaceAscIIChar(String s) {
        if (!s.contains("&#")) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i != s.length(); ++i) {
            if (s.charAt(i) == '&' && i + 1 < s.length() && s.charAt(i + 1) == '#') {
                int j = i + 2;
                for (; j < s.length() && s.charAt(j) != ';'; ++j);
                if (j < s.length()) {
                    char a = (char) Integer.parseInt(s.substring(i + 2, j));
                    stringBuilder.append(a);
                    i = j;
                }
            } else {
                stringBuilder.append(s.charAt(i));
            }
        }
        return stringBuilder.toString();
    }
}
