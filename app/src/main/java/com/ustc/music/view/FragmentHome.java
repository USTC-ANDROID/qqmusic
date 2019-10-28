package com.ustc.music.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lany.banner.BannerAdapter;
import com.lany.banner.BannerView;
import com.ustc.music.R;
import com.ustc.music.activity.PlayActivity;
import com.ustc.music.activity.RankActivity;
import com.ustc.music.activity.RecommentListActivity;
import com.ustc.music.activity.SearchActivity;
import com.ustc.music.activity.SingersActivity;
import com.ustc.music.activity.TypeActivity;
import com.ustc.music.adapter.HorizontalListViewAdapter;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.util.SmallUtil;

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

public class FragmentHome extends LazyBaseFragment implements AdapterView.OnItemClickListener {


    private BannerView bannerView; //轮播
    private List<String> bannerImgs = new ArrayList<>(); //轮播数据
    private HorizontalListView guangFangGeDanListView; //官方歌单
    private HorizontalListView daRenGeDanListView; //达人歌单
    private HorizontalListView zuiXinZhuanJIListView; //最新专辑


    private HorizontalListViewAdapter guanFangGeDanAdapter; //官方歌单列表适配器
    private HorizontalListViewAdapter daRenAdapter;
    private HorizontalListViewAdapter zuiXinZhuanJiAdapter; //最新专辑适配器


    private List<Map<String, String>> guanFangGeDanDataSource = new ArrayList<>(); //官方歌单列表数据源
    private List<Map<String, String>> daRenDataSource = new ArrayList<>(); //官方歌单列表数据源
    private List<Map<String, String>> zuiXinZhuanJiDataSource = new ArrayList<>(); //最新专辑数据源

    LinearLayout rankBtn;
    LinearLayout typeBtn;
    LinearLayout searchBtn;

    @Override
    protected void findViewById(View rootView) {
        bannerView = rootView.findViewById(R.id.banner_view);
        guangFangGeDanListView = rootView.findViewById(R.id.guanfang).findViewById(R.id.hlv);
        daRenGeDanListView = rootView.findViewById(R.id.daren).findViewById(R.id.hlv);
        zuiXinZhuanJIListView = rootView.findViewById(R.id.zuixinzhuanji).findViewById(R.id.hlv);
        rankBtn = rootView.findViewById(R.id.rankBtn);
        typeBtn = rootView.findViewById(R.id.typeBtn);
        searchBtn = rootView.findViewById(R.id.searchBtn);
    }


    private void initEvent() {
        zuiXinZhuanJIListView.setOnItemClickListener(this);
        daRenGeDanListView.setOnItemClickListener(this);
        guangFangGeDanListView.setOnItemClickListener(this);
        rankBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), RankActivity.class);
            startActivity(intent);
        });

        typeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), TypeActivity.class);
            startActivity(intent);
        });

        searchBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), SearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected int setContentView() {
        return R.layout.fragement_menu_home;
    }

    @Override
    protected void lazyLoad() {
//        这里进行网络请求
        initAdapter();
        initData();
        initEvent();
    }

    private void initBanner() {
        bannerView.setAdapter(new BannerAdapter<String>(bannerImgs) {
            @Override
            public void bindImage(ImageView imageView, String item) {
                //下载图片
                Glide.with(getContext())
                        .load(item)
                        .placeholder(R.drawable.banner_default)
                        .dontAnimate()
                        .error(R.drawable.banner_default)
                        .into(imageView);

            }

            @Override
            public void bindTitle(TextView titleText, String item) {

            }

            @Override
            public void onItemClicked(int position, String item) {

            }
        });
    }

    protected void initData() {

        initZuiXinGeDan();
        initDaRenGeDan();
        initGuanFangGeDan();
        /**
         * 获取轮播图片
         */
        RequestUtil.get(DataUrl.Banner, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.v("net", "fail" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResponseBody body = response.body();
                        try {
                            JSONObject json = new JSONObject(body.string());
                            JSONArray jsonArray = json.getJSONObject("focus").getJSONObject("data")
                                    .getJSONArray("content");
                            for(int i = 0; i < jsonArray.length(); i++) {
                                System.out.println("轮播的大小" + jsonArray.length());
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                try {
                                    bannerImgs.add(jsonObject.getJSONObject("pic_info").getString("url"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Log.d("net", "success1" + bannerImgs.get(bannerImgs.size() - 1));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initBanner();
                    }
                });
            }
        });
    }



    private void initGuanFangGeDan() {
        RequestUtil.get(DataUrl.GuangFangGeDan, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

        }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                try {
                    JSONObject json = new JSONObject(string);
                    final JSONArray jsonArray = json.getJSONObject("playlist").getJSONObject("data")
                            .getJSONArray("v_playlist");

                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String coverUrl = jsonObject.getString("cover_url_big");
                        String title = jsonObject.getString("title");
                        String access_num = SmallUtil.accessNumFormat(jsonObject.getInt("access_num"));
                        String tid = jsonObject.getString("tid");
                        Map<String, String> data = new HashMap<>();
                        data.put("url", DataUrl.guanFangGeDanDetail.replace("{1}", tid));

                        data.put("id", tid);
                        data.put("coverUrl", coverUrl);
                        data.put("access_num", access_num);
                        data.put("title", title);
                        guanFangGeDanDataSource.add(data);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            guanFangGeDanAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initDaRenGeDan() {
        RequestUtil.get(DataUrl.DaRenGeDan, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                String string = body.string();
                try {
                    JSONObject json = new JSONObject(string);
                    JSONArray jsonArray = json.getJSONObject("recomPlaylist")
                            .getJSONObject("data")
                            .getJSONArray("v_hot");
                    int len = jsonArray.length();
                    for(int i = 0; i < len; i++) {
                        Map<String, String> data = new HashMap<>();
                        data.put("access_num", SmallUtil.accessNumFormat(jsonArray.getJSONObject(i).getInt("listen_num")));
                        data.put("title", jsonArray.getJSONObject(i).getString("title"));
                        data.put("url", "https://y.qq.com/n/yqq/playsquare/{1}.html".replace("{1}", jsonArray.getJSONObject(i).getString("content_id")));
                        data.put("id", jsonArray.getJSONObject(i).getString("content_id"));
                        data.put("coverUrl", jsonArray.getJSONObject(i).getString("cover"));
                        daRenDataSource.add(data);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            daRenAdapter.notifyDataSetChanged();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initZuiXinGeDan() {
        RequestUtil.get(DataUrl.zuiXinZhuanJi, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                Log.v("mainactivity", string);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    Log.v("coverUrl", string);
                    JSONArray jsonArray = jsonObject.getJSONObject("playlist")
                            .getJSONObject("data")
                            .getJSONArray("v_playlist");
                    int len = jsonArray.length();
                    Log.v("coverUrl", len + "");
                    for(int i = 0; i < len; i++) {
                        Log.v("coverUrl", "测试");
                        Map<String, String> data = new HashMap<>();
                        String title = jsonArray.getJSONObject(i).getString("title");
                        String coverUrl = jsonArray.getJSONObject(i).getString("cover_url_big");
                        String tid = jsonArray.getJSONObject(i).getString("tid");
                        data.put("access_num", SmallUtil.accessNumFormat(jsonArray.getJSONObject(i).getInt("access_num")));
                        data.put("title", title);
                        data.put("id", tid);
                        data.put("coverUrl", coverUrl);
                        zuiXinZhuanJiDataSource.add(data);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            zuiXinZhuanJiAdapter.notifyDataSetChanged();
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = (String) view.getTag(R.id.title);
        Intent intent = new Intent(getContext(), RecommentListActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("id", String.valueOf(value));

        intent.putExtra("id", value);
//        intent.
        intent.putExtra("bundle", bundle);
        startActivity(intent);
//        Toast.makeText(this, value, Toast.LENGTH_LONG).show();
    }

    protected void initAdapter() {
        guanFangGeDanAdapter = new HorizontalListViewAdapter(getContext(),  guanFangGeDanDataSource);
        guangFangGeDanListView.setAdapter(guanFangGeDanAdapter);
        daRenAdapter = new HorizontalListViewAdapter(getContext(), daRenDataSource);
        daRenGeDanListView.setAdapter(daRenAdapter);
        zuiXinZhuanJiAdapter = new HorizontalListViewAdapter(getContext(), zuiXinZhuanJiDataSource);
        zuiXinZhuanJIListView.setAdapter(zuiXinZhuanJiAdapter);
    }

    public void toSingersActivity(View view) {
        Intent intent = new Intent(getContext(), SingersActivity.class);
        startActivity(intent);
    }


    public void toPage(View view) {
        Intent intent = new Intent(getContext(), PlayActivity.class);
        startActivity(intent);

    }
}
