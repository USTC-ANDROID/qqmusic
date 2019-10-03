package com.ustc.music.util;

import android.provider.MediaStore;

import java.io.IOException;

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
        Request.Builder builder = new Request.Builder();
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
