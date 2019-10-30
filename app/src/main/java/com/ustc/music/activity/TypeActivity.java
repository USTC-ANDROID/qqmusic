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

    private final Integer[] typeNameId = {R.id.typeTextView1, R.id.typeTextView2, R.id.typeTextView3,
            R.id.typeTextView4, R.id.typeTextView5, R.id.typeTextView6};

    private final Integer[] imgId = {R.id.albumImageView1, R.id.albumImageView2, R.id.albumImageView3,
            R.id.albumImageView4, R.id.albumImageView5, R.id.albumImageView6, R.id.albumImageView7,
            R.id.albumImageView8, R.id.albumImageView9, R.id.albumImageView10, R.id.albumImageView11,
            R.id.albumImageView12, R.id.albumImageView13, R.id.albumImageView14, R.id.albumImageView15,
            R.id.albumImageView16, R.id.albumImageView17, R.id.albumImageView18, R.id.albumImageView19,
            R.id.albumImageView20, R.id.albumImageView21, R.id.albumImageView22, R.id.albumImageView23,
            R.id.albumImageView24};

    private final Integer[] dissNameId = {R.id.albumTextView1_1, R.id.albumTextView2_1, R.id.albumTextView3_1,
            R.id.albumTextView4_1, R.id.albumTextView5_1, R.id.albumTextView6_1, R.id.albumTextView7_1,
            R.id.albumTextView8_1, R.id.albumTextView9_1, R.id.albumTextView10_1, R.id.albumTextView11_1,
            R.id.albumTextView12_1, R.id.albumTextView13_1, R.id.albumTextView14_1, R.id.albumTextView15_1,
            R.id.albumTextView16_1, R.id.albumTextView17_1, R.id.albumTextView18_1, R.id.albumTextView19_1,
            R.id.albumTextView20_1, R.id.albumTextView21_1, R.id.albumTextView22_1, R.id.albumTextView23_1,
            R.id.albumTextView24_1};

    private final Integer[] authorId = {R.id.albumTextView1_2, R.id.albumTextView2_2, R.id.albumTextView3_2,
            R.id.albumTextView4_2, R.id.albumTextView5_2, R.id.albumTextView6_2, R.id.albumTextView7_2,
            R.id.albumTextView8_2, R.id.albumTextView9_2, R.id.albumTextView10_2, R.id.albumTextView11_2,
            R.id.albumTextView12_2, R.id.albumTextView13_2, R.id.albumTextView14_2, R.id.albumTextView15_2,
            R.id.albumTextView16_2, R.id.albumTextView17_2, R.id.albumTextView18_2, R.id.albumTextView19_2,
            R.id.albumTextView20_2, R.id.albumTextView21_2, R.id.albumTextView22_2, R.id.albumTextView23_2,
            R.id.albumTextView24_2};

    private List<Map<String, String>> datas = new ArrayList<>();

    private final String []cId = {"6", "15", "39", "136", "52", "122"};
    private final String []Type = {"流行歌单", "轻音乐", "ACG分享", "经典歌单", "伤感", "安静"};



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.type_activity);

        StatusBarUtils.setWindowStatusBarColor(this, R.color.menu_color);

        initData();
        initAdapter();
    }

    private void initData() {

        final Map<String, String> type = new HashMap<>();
        final Map<String, String> headers = new HashMap<String, String>();
        final int []flag = {0, 0, 0, 0, 0, 0};
        headers.put("referer", "https://y.qq.com/portal/playlist.html");

        for (int i = 0; i < 6; i++) {

            type.put(cId[i], Type[i]);

            String TypeUrl = "https://c.y.qq.com/splcloud/fcgi-bin/fcg_get_diss_by_tag.fcg?" +
                    "picmid=1&rnd=0.853558521310884&g_tk=5381&loginUin=0&hostUin=0&format=json" +
                    "&inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq.json&needNewCode=0&" +
                    "categoryId=" + cId[i] + "&sortId=5&sin=0&ein=19&referer=https://y" +
                    ".qq.com/portal/playlist.html";

            System.out.println(Type[i]);

            RequestUtil.get(TypeUrl, headers, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("failure");
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        Map<String, String> data = new HashMap<>();
                        String callStr = response.body().string();
                        JSONObject call_json = new JSONObject(callStr);
                        JSONArray dissList = call_json.getJSONObject("data").getJSONArray("list");
                        String categoryId = call_json.getJSONObject("data").getString("categoryId");
                        //System.out.println("categoryId：" + categoryId);
                        data.put("categoryId", categoryId);
                        data.put("typeName", type.get(categoryId));
                        System.out.println(data.get("categoryId") + "  " + data.get("typeName"));
                        for (int j = 0; j < dissList.length(); j++) {
                            if (j < 4) {
                                JSONObject dissItem = dissList.getJSONObject(j);
                                String dissName = dissItem.getString("dissname");
                                String imgUrl = dissItem.getString("imgurl");
                                String author = dissItem.getJSONObject("creator").getString("name");
                                data.put("dissName" + j, dissName);
                                data.put("imgUrl" + j, imgUrl);
                                data.put("author" + j, author);
                                //System.out.println(dissName + "  " + author + "\n" + imgUrl);
                            } else
                                break;
                        }
                        datas.add(data);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int typeNum = 0;
                                for (int i = 0; i < 6; i++) {
                                    if (flag[i] == 0) {
                                        typeNum = i;
                                        flag[i] = 1;
                                        break;
                                    }
                                }
                                int i = typeNum;
                                Map<String, String> data = datas.get(i);
                                TextView typeNameText = (TextView) findViewById(typeNameId[i]);
                                typeNameText.setText(data.get("typeName"));
                                for (int j = 0; j < 4; j++){
                                    int index = i * 4 + j;
                                    ImageView imgView = (ImageView) findViewById(imgId[index]);
                                    System.out.println(i + "  " + j + "  " + index);
                                    Glide.with(TypeActivity.this)
                                            .load(data.get("imgUrl" + j))
                                            .placeholder(R.drawable.banner_default)
                                            .dontAnimate()
                                            .error(R.drawable.banner_default)
                                            .into(imgView);
                                    TextView dissNameText = (TextView) findViewById(dissNameId[index]);
                                    dissNameText.setText(data.get("dissName" + j));
                                    TextView authorText = (TextView) findViewById(authorId[index]);
                                    authorText.setText(data.get("author" + j));
                                }
                            }
                        });
                        //System.out.println(datas.get(0));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    private void initView() {

    }


    private void initAdapter() {

    }


}
