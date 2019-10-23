package com.ustc.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.base.BaseActivity;
import com.ustc.music.entity.Music;
import com.ustc.music.service.SimpleService;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.view.ImageHeadScrollView;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecommentListActivity extends BaseActivity implements View.OnClickListener {

    private String url;
    private String id;
    private ImageHeadScrollView imageHeadScrollView;
    private LinearLayout musicsListView;
    private List<Map<String, String>> datas = new ArrayList<>();
    private SimpleAdapter musicListViewAdapter = null;
    private ImageView imageView;
    private TextView dissnameTextView;
    private TextView descTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreate(savedInstanceState, R.layout.activity_recomment);


    }

    @Override
    protected void initView() {
        imageHeadScrollView = findViewById(R.id.image_scroll_view);
        Log.v("RecommentListActivity", "滚动");
//        imageHeadScrollView.scrollTo(0, 1000);
        musicsListView = findViewById(R.id.musics);
        imageView = findViewById(R.id.scroll_view_headimage);
        dissnameTextView = findViewById(R.id.dissname);
        descTextView = findViewById(R.id.desc);
        this.bottomTabsLayout.setTabsHidden();

    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");

        id = bundle.getString("id");
        Log.v("RECOMMENTLISTACTIVITY", id);
        Map<String, String> map = new HashMap<>();
        map.put("referer", "https://y.qq.com/n/yqq/playsquare/");
        RequestUtil.get(DataUrl.musicList.replace("{1}", id), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                JSONObject jsonObj = JSONObject.parseObject(string);
                Log.v("json", string);
                JSONObject cdlist = jsonObj.getJSONArray("cdlist").getJSONObject(0);
                final String dissname = cdlist.getString("dissname");
                final String desc = cdlist.getString("desc");
                final String logo = cdlist.getString("logo").replace("300", "600");
                JSONArray songlist = cdlist.getJSONArray("songlist");
                for(int i = 0; i < songlist.size(); i++) {
                    JSONObject jsonObject = songlist.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String name = jsonObject.getJSONArray("singer").getJSONObject(0).getString("name");
                    String mid = jsonObject.getString("mid");
                    String imgMid = jsonObject.getJSONObject("album").getString("mid");
                    Map<String, String> data = new HashMap<>();
                    data.put("imgMid", imgMid);
                    data.put("title", title);
                    data.put("name", name);
                    data.put("mid", mid);
                    data.put("index", i + 1 + "");
                    datas.add(data);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        musicListViewAdapter.notifyDataSetChanged();
//                        descTextView.setText(desc);
//                        dissnameTextView.setText(dissname);
//                        Glide.with(RecommentListActivity.this)
//                                .load(logo)
//                                .error(R.drawable.newmusic)
//                                .placeholder(R.drawable.newmusic)
//                                .into(imageView);
                        for (int i = 0; i < datas.size(); i++) {
                            Map<String, String> data = datas.get(i);
                            View view = LinearLayout.inflate(RecommentListActivity.this, R.layout.music_list_item, null);
                            TextView indexView = view.findViewById(R.id.music_index);
                            TextView nameView = view.findViewById(R.id.music_name);
                            TextView authorView = view.findViewById(R.id.music_author);
                            indexView.setText(data.get("index"));
                            nameView.setText(data.get("title"));
                            authorView.setText(data.get("name"));
                            view.setTag( R.id.tag_first, data.get("mid"));
                            view.setTag(R.id.tag_second, data.get("imgMid"));
                            view.setOnClickListener(RecommentListActivity.this);
                            musicsListView.addView(view);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void initAdapter() {
    }



    @Override
    public void onClick(View v) {
        final String mid = String.valueOf(v.getTag(R.id.tag_first));
        TextView tv = v.findViewById(R.id.music_name);
        final String title = tv.getText().toString();
        final String imgMid = String.valueOf(v.getTag(R.id.tag_second));
        Map<String, String> map = new HashMap<>();
        map.put("Referer", "https://y.qq.com/");
        RequestUtil.get(DataUrl.playMusicStep1.replace("{1}", mid), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                final String musicSource = JSONObject.parseObject(string)
                        .getJSONObject("req_0")
                        .getJSONObject("data")
                        .getJSONArray("midurlinfo")
                        .getJSONObject(0).getString("purl");

//                RequestUtil.get();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playService.add(new Music(DataUrl.musicLogo.replace("{1}", imgMid)
                                ,title, DataUrl.musicLrc.replace("{1}", mid), DataUrl.playMusicStep2.replace("{1}", musicSource)));
                        playService.play();
                        bottomTabsLayout.refershMusic(DataUrl.musicLogo.replace("{1}", imgMid), title);
                    }
                });


//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        final MediaPlayer player = new MediaPlayer();
//                        try {
//                            Log.v("music_url", DataUrl.playMusicStep2.replace("{1}", musicSource));
//                            player.setDataSource(DataUrl.playMusicStep2.replace("{1}", musicSource));
//                            player.prepareAsync();
//                            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                                @Override
//                                public void onPrepared(MediaPlayer mp) {
//                                    Toast.makeText(RecommentListActivity.this, "准备完毕", Toast.LENGTH_LONG).show();
//                                    player.start();
//                                }
//                            });
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });

            }
        });
    }
}
