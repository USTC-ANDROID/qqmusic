package com.ustc.music.util;

import android.provider.MediaStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class RequestUtil {

    private static OkHttpClient client = new OkHttpClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");



    public static void get(String url, Callback callBack) {
        get(url, new HashMap<String, String>(), callBack);
    }

    public static void get(String url, Map<String, String> datas, Callback callBack) {
        Request.Builder builder = new Request.Builder();
//        Request request = builder.get().url(url).build();
        Set<String> sets = datas.keySet();
        for(String key : sets) builder.addHeader(key, datas.get(key));
        builder.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; " +
                "x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
        Request request = builder.get().url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callBack);
    }

    public static void post(String url, FormBody.Builder formData, Callback callBack) {
        Request.Builder builder = new Request.Builder();
        RequestBody body = formData.build();
        Request request = builder.post(body).url(url).build();
        Call call = client.newCall(request);
        call.enqueue(callBack);
    }

    public static void postJSON(String url, String json, Callback callBack) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callBack);
    }

}
