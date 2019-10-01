package com.ustc.music.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.banner.BannerAdapter;
import com.lany.banner.BannerView;
import com.ustc.music.R;
import com.ustc.music.adapter.HorizontalListViewAdapter;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.util.StatusBarUtils;
import com.ustc.music.view.HorizontalListView;

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

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    private List<String> bannerImgs = new ArrayList<>();

    private HorizontalListView horizontalListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        initData();
        initAdapter();
    }

    private void initView() {
        bannerView = (BannerView)findViewById(R.id.banner_view);
        horizontalListView = (HorizontalListView)findViewById(R.id.hlv);
    }

    private void initData() {
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
                runOnUiThread(new Runnable() {
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
                        bannerView.setAdapter(new BannerAdapter<String>(bannerImgs) {
                            @Override
                            public void bindImage(ImageView imageView, String item) {
                                //下载图片
                                Glide.with(MainActivity.this)
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
                        Toast.makeText(MainActivity.this, "content", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        int screenWidth = getWindowManager().getDefaultDisplay().getWidth(); // 屏幕宽（像素，如：480px）
        final String[] strings = new String[]{"零售", "零退", "批销", "批退", "调剂"};

        HorizontalListViewAdapter horizontalListViewAdapter = new HorizontalListViewAdapter(getApplicationContext(), screenWidth, strings);
        horizontalListView.setAdapter(horizontalListViewAdapter);

        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this ,strings[position], 1).show();
            }
        });
    }

    private void initAdapter() {

    }

}
