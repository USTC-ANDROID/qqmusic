package com.ustc.music.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ustc.music.entity.Music;
import com.ustc.music.url.DataUrl;
import com.ustc.music.util.RequestUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MiGuMusicSource {

    public static void loadMusicSourceFromMiGu(final String title, String author, final MusicRequestCallBack callBack) {
        Map<String, String> map = new HashMap<>();
        map.put("Referer", "http://m.music.migu.cn/");
        RequestUtil.get(DataUrl.miGuSearch.replace("{1}", title + " " + author), map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().string();
                JSONArray array = JSONObject.parseObject(string)
                        .getJSONArray("musics");
                String source = null;
                if(array.size() != 0) {
                    source = array.getJSONObject(0).getString("mp3");
                }
                callBack.call(source);
//                RequestUtil.get();

            }
        });
    }

}
