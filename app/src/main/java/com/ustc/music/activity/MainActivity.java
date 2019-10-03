package com.ustc.music.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lany.banner.BannerAdapter;
import com.lany.banner.BannerView;
import com.ustc.music.R;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.util.StatusBarUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private BannerView bannerView;

    private List<String> bannerImgs = new ArrayList<>();

    LinearLayout rankBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        initData();
        //testInitData();
        initAdapter();

        // 跳转
        rankBtn = (LinearLayout)findViewById(R.id.rankBtn);
        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "点击", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        bannerView = (BannerView)findViewById(R.id.banner_view);
    }

    private void testInitData(){
        /**
         * 尝试获取排行榜歌曲
         * url = https://u.y.qq.com/cgi-bin/musicu.fcg?_=1569934525603
         * method = POST
         */
        String url = "https://u.y.qq.com/cgi-bin/musicu.fcg?_=1569934525603";

        FormBody.Builder data = new FormBody.Builder();
//        data.add("req_0", "{\"module\":\"QQConnectLogin.LoginMethod\",\"method\":\"GetLoginMethod\",\"param\":{\"id\":1569933954420}}");
//        data.add("comm", "")
        data.add("req_0", "{\"module\":\"QQConnectLogin.LoginMethod\",\"method\":\"GetLoginMethod\",\"param\":{\"id\":1569933954420}}");
        data.add("comm", "{\"g_tk\":550260307,\"uin\":782562661,\"format\":\"json\",\"ct\":23,\"cv\":0}");
        RequestUtil.post(url, data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ResponseBody body = response.body();
                        try {
                            String s = body.string();
                            System.out.println("输出ResponseBody：" + s);
                            Toast.makeText(MainActivity.this, s.substring(0, 10), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
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
    }

    private void initAdapter() {

    }


}
