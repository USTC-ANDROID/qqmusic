package com.ustc.music.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class RequestUtil {

    private static OkHttpClient client = new OkHttpClient();



    public static void get(String url, Callback callBack) {
        get(url, new HashMap<String, String>(), callBack);
    }

    public static void get(String url, Map<String, String> datas, Callback callBack) {
        Request.Builder builder = new Request.Builder();
//        Request request = builder.get().url(url).build();
        Set<String> sets = datas.keySet();
        for(String key : sets) builder.addHeader(key, datas.get(key));
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

}
