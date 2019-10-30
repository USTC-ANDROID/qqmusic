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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class RankActivity extends Activity {

    Button backBtn;
    private List<Map<String, String>> datas = new ArrayList<>();

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_activity);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);
        initView();
        //initData();
        try {
            initData();
            initEvent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // initAdapter();
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
                try {
                    String callStr = response.body().string();
                    JSONObject call_json = new JSONObject(callStr);
                    JSONArray groupList = call_json.getJSONObject("req_0").getJSONObject("data").
                            getJSONArray("group");
                    int count = 0;
                    for (int i = 0; i < groupList.length(); i++) {
                        JSONArray topList = groupList.getJSONObject(i).getJSONArray("toplist");
                        for (int j = 0; j < topList.length(); j++) {
                            if (count < 16) {
                                Map<String, String> data = new HashMap<>();
                                JSONObject rankItem = topList.getJSONObject(j);
                                String topId = rankItem.getString("topId");
                                System.out.println("榜单ID：" + topId);
                                String rankTitle = rankItem.getString("title");
                                if (rankTitle.contains("MV榜"))
                                    continue;
                                System.out.println("榜单名: " + rankTitle);
                                String picUrl = rankItem.getString("mbFrontPicUrl");
                                System.out.println("图片url: " + picUrl);
                                String period = rankItem.getString("period");
                                data.put("topId", topId);
                                data.put("rankTitle", rankTitle);
                                data.put("picUrl", picUrl);
                                data.put("period", period);
                                JSONArray songList = rankItem.getJSONArray("song");
                                for (int k = 0; k < songList.length(); k++) {
                                    JSONObject songItem = songList.getJSONObject(k);
                                    String song = songItem.getString("title")
                                            + " - " + songItem.getString("singerName");
                                    System.out.println("歌名：" + song);
                                    // String key = "song_" + k;
                                    data.put("song" + k, song);
                                }
                                datas.add(data);
                            }
                            else break;
                            count ++;
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 0; i < 15; i++) {
                                Map<String, String> data = datas.get(i);
                                TextView textView = (TextView) findViewById(rankTitleId[i]);
                                textView.setText(data.get("rankTitle"));
                                ImageView imgView = (ImageView) findViewById(imgId[i]);
                                Glide.with(RankActivity.this)
                                        .load(data.get("picUrl"))
                                        .placeholder(R.drawable.banner_default)
                                        .dontAnimate()
                                        .error(R.drawable.banner_default)
                                        .into(imgView);
                                imgView.setTag(data);
//                                Object tag = imgView.getTag();
                                for (int j = 0; j < 3; j++) {
                                    TextView songTextView = (TextView) findViewById(rankItemId[i][j]);
                                    songTextView.setText(data.get("song" + j));
                                }
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initAdapter() {

    }

    private void initEvent() {
        for (int id : imgId) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Map<String, String> data = (Map<String, String>) view.getTag();
                    String topId = data.get("topId");
                    String period = data.get("period");
                    String getSongMidUrl = "https://u.y.qq.com/cgi-bin/musicu.fcg?-=getUCGI" +
                            "1982209349874262&g_tk=1756517747&loginUin=782562661&hostUin=0&" +
                            "format=json&inCharset=utf8&outCharset=utf-8&notice=0&platform=" +
                            "yqq.json&needNewCode=0&data={\"detail\":{\"module\":\"musicTop" +
                            "list.ToplistInfoServer\",\"method\":\"GetDetail\",\"param\":{\"" +
                            "topId\":" + topId + ",\"offset\":0,\"num\":20,\"period\":\"" +
                            period + "\"}},\"" + "comm\":{\"ct\":24,\"cv\":0}}";

                    RequestUtil.get(getSongMidUrl, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try{
                                String res = response.body().string();
                                JSONObject res_json = new JSONObject(res);
//                                JSONArray jsonArray = res_json.getJSONObject("detail")
//                                        .getJSONObject("data").getJSONArray("songInfoList");
//                                if(jsonArray.length() == 0) jsonArray = res_json.getJSONObject("detail")
//                                        .getJSONObject("data").getJSONObject("data")
//                                        .getJSONArray("song");
                                String songMid = res_json.getJSONObject("detail")
                                        .getJSONObject("data").getJSONArray("songInfoList")
                                        .getJSONObject(0).getString("mid");
                                String albumMid = res_json.getJSONObject("detail")
                                        .getJSONObject("data").getJSONArray("songInfoList")
                                        .getJSONObject(0).getJSONObject("album")
                                        .getString("mid");
                                System.out.println("songMid:" + songMid);
                                System.out.println("albumMid:" + albumMid);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });
        }
    }
}
