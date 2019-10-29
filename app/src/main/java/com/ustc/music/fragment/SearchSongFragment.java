package com.ustc.music.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.ustc.music.R;
import com.ustc.music.adapter.SearchSongRecyclerViewAdapter;
import com.ustc.music.core.MiGuMusicSource;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.view.SmileToast;

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
        Map<String, String> map = new HashMap<>();
        map.put("Referer", "https://y.qq.com/");
        Map<String, String> stringStringMap = songSingerDataSource.get((Integer) v.getTag());
        play(stringStringMap.get("songmid"), map, stringStringMap.get("singerName"), stringStringMap.get("songName"), stringStringMap.get("albummid"));
    }

    private void play(final String mid, final Map<String, String> map, final String author, final String title, final String imgMid) {
        RequestUtil.get(DataUrl.playMusicStep1.replace("{1}", mid), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.v("musicsource", string);
                final String musicSource = com.alibaba.fastjson.JSONObject.parseObject(string)
                        .getJSONObject("req_0")
                        .getJSONObject("data")
                        .getJSONArray("midurlinfo")
                        .getJSONObject(0).getString("purl");
                if("".equals(musicSource)) {

                    MiGuMusicSource.loadMusicSourceFromMiGu(title, author, source -> getActivity().runOnUiThread(() -> {
                        SmileToast.makeSmileToast(getActivity(),
                                "对不起，QQ音乐源不能播放，正为你切换到咪咕音乐源",
                                SmileToast.LENGTH_LONG).show();
                        playMusic(imgMid, title, mid, source);
                    }));
                } else {
                    getActivity().runOnUiThread(() -> {

                        Log.v("musicsource", musicSource);
                        String source = musicSource;
                        if(source.contains("qq.com")) {
                            source = source.substring(source.indexOf("qq.com/C") + 7);
                        }
                        playMusic(imgMid, title, mid, DataUrl.playMusicStep2.replace("{1}", source));
                    });
                }
            }
        });
    }

    protected void playMusic(final String imgMid, final String title, final String mid, final String musicSource) {
        System.out.println("abc");

//        playService.add(new Music(DataUrl.musicLogo.replace("{1}", imgMid)
//                ,title, DataUrl.musicLrc.replace("{1}", mid), musicSource));
//        playService.playNext();
//        bottomTabsLayout.refershMusic(DataUrl.musicLogo.replace("{1}", imgMid), title);
    }
}
