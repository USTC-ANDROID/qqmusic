package com.ustc.music.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RankActivity extends Activity {

    Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.rank_activity);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        //initData();
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initAdapter();

        // 返回
        backBtn = (Button)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RankActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void initView() {

    }

    private void initData() throws IOException {

        final String jsonString = "{\"req_0\":{\"module\":\"musicToplist.ToplistInfoServer\",\"" +
                "method\":\"GetAll\",\"param\":{}},\"comm\":{\"g_tk\":1610275164,\"uin\":78" +
                "2562661,\"format\":\"json\",\"ct\":23,\"cv\":0}}";

        final Integer []imgId = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4,
                R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8, R.id.imageView9,
                R.id.imageView10, R.id.imageView11, R.id.imageView12, R.id.imageView13, R.id.imageView14,
                R.id.imageView15, };

        final Integer []rankTitleId = {R.id.rank1, R.id.rank2, R.id.rank3, R.id.rank4, R.id.rank5,
                R.id.rank6, R.id.rank7, R.id.rank8, R.id.rank9, R.id.rank010, R.id.rank011, R.id.rank012,
                R.id.rank013, R.id.rank014, R.id.rank015, };

        final Integer [][]rankItemId = {{R.id.rank11, R.id.rank12, R.id.rank13},
                {R.id.rank21, R.id.rank22, R.id.rank23}, {R.id.rank31, R.id.rank32, R.id.rank33},
                {R.id.rank41, R.id.rank42, R.id.rank43}, {R.id.rank51, R.id.rank52, R.id.rank53},
                {R.id.rank61, R.id.rank62, R.id.rank63}, {R.id.rank71, R.id.rank72, R.id.rank73},
                {R.id.rank81, R.id.rank82, R.id.rank83}, {R.id.rank91, R.id.rank92, R.id.rank93},
                {R.id.rank101, R.id.rank102, R.id.rank103}, {R.id.rank111, R.id.rank112, R.id.rank113},
                {R.id.rank121, R.id.rank122, R.id.rank123}, {R.id.rank131, R.id.rank132, R.id.rank133},
                {R.id.rank141, R.id.rank142, R.id.rank143}, {R.id.rank151, R.id.rank152, R.id.rank153}};

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestUtil.postJSON(DataUrl.RankUrl, jsonObject.toString(), new Callback() {
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
                            JSONArray groupList = call_json.getJSONObject("req_0").getJSONObject("data").
                                    getJSONArray("group");
                            int count = 0;
                            for (int i = 0; i < groupList.length(); i++) {
                                JSONArray topList = groupList.getJSONObject(i).getJSONArray("toplist");
                                for (int j = 0; j < topList.length(); j++) {
                                    JSONObject rankItem = topList.getJSONObject(j);
                                    System.out.println("榜单名: " + rankItem.getString("title"));
                                    String rankTitle = rankItem.getString("title");
                                    JSONArray songList = rankItem.getJSONArray("song");
                                    System.out.println("图片url: " + rankItem.getString("mbFrontPicUrl"));
                                    String picUrl = rankItem.getString("mbFrontPicUrl");

                                    if(count < 15) {
                                        TextView textView = (TextView)findViewById(rankTitleId[count]);
                                        textView.setText(rankTitle);
                                        ImageView imgView = (ImageView)findViewById(imgId[count]);
                                        Glide.with(RankActivity.this)
                                                .load(rankItem.getString("mbFrontPicUrl"))
                                                .placeholder(R.drawable.banner_default)
                                                .dontAnimate()
                                                .error(R.drawable.banner_default)
                                                .into(imgView);
                                        for (int k = 0; k < songList.length(); k++) {
                                            JSONObject songItem = songList.getJSONObject(k);
                                            System.out.println("  歌名: " + songItem.getString("title")
                                                    + " - " + songItem.getString("singerName"));
                                            String song = songItem.getString("title")
                                                    + " - " + songItem.getString("singerName");
                                            TextView songTextView = (TextView)findViewById(rankItemId[count][k]);
                                            songTextView.setText(song);
                                        }
                                    }
                                    count ++;

                                }
                            }
                            System.out.println("总共这么多个：" + (count));

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    private void initAdapter() {

    }
}
