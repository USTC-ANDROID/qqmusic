package com.ustc.music.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ustc.music.R;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;
import com.ustc.music.util.StatusBarUtils;

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
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class TypeActivity extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.type_activity);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        initData();
        initAdapter();
    }

    private void initData() {


        final Integer []imgId = {R.id.albumImageView1, R.id.albumImageView2, R.id.albumImageView3,
                R.id.albumImageView4, R.id.albumImageView5, R.id.albumImageView6, R.id.albumImageView7,
                R.id.albumImageView8, R.id.albumImageView9, R.id.albumImageView10, R.id.albumImageView11,
                R.id.albumImageView12};

        final Integer []dissNameId = {R.id.albumTextView1_1, R.id.albumTextView2_1, R.id.albumTextView3_1,
                R.id.albumTextView4_1, R.id.albumTextView5_1, R.id.albumTextView6_1, R.id.albumTextView7_1,
                R.id.albumTextView8_1, R.id.albumTextView9_1, R.id.albumTextView10_1, R.id.albumTextView11_1,
                R.id.albumTextView12_1};

        final Integer []authorId = {R.id.albumTextView1_2, R.id.albumTextView2_2, R.id.albumTextView3_2,
                R.id.albumTextView4_2, R.id.albumTextView5_2, R.id.albumTextView6_2, R.id.albumTextView7_2,
                R.id.albumTextView8_2, R.id.albumTextView9_2, R.id.albumTextView10_2, R.id.albumTextView11_2,
                R.id.albumTextView12_2};




        Map<String, String> headers = new HashMap<String, String>();
        headers.put("referer", "https://y.qq.com/portal/playlist.html");

        RequestUtil.get(DataUrl.TypeUrl, headers, new Callback(){

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String callStr = response.body().string();
                            JSONObject call_json = new JSONObject(callStr);
                            JSONArray dissList = call_json.getJSONObject("data").getJSONArray("list");
                            int count = 0;
                            for (int i = 0; i < dissList.length(); i++) {
                                count ++;
                                JSONObject dissItem = dissList.getJSONObject(i);
                                String dissName = dissItem.getString("dissname");
                                String imgUrl = dissItem.getString("imgurl");
                                String author = dissItem.getJSONObject("creator").getString("name");
                                System.out.println(dissName + "  " + author + "\n" + imgUrl);
                                ImageView imgView = (ImageView)findViewById(imgId[i]);
                                Glide.with(TypeActivity.this)
                                        .load(dissItem.getString("imgurl"))
                                        .placeholder(R.drawable.banner_default)
                                        .dontAnimate()
                                        .error(R.drawable.banner_default)
                                        .into(imgView);
                                TextView dissNameText = (TextView)findViewById(dissNameId[i]);
                                dissNameText.setText(dissName);
                                TextView authorText = (TextView)findViewById(authorId[i]);
                                authorText.setText(author);
                                if (count == 12)
                                    break;
                            }

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();

                        }
                    }
                });

            }
        });

    }

    private void initView() {
    }

    private void initAdapter() {

    }


}
