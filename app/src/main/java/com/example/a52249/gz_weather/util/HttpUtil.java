package com.example.a52249.gz_weather.util;

import okhttp3.OkHttpClient;


/**
 * Created by 52249 on 2019/6/14.
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        /*
        与服务器进行交互的代码，并且注册一个回调处理服务器响应
         */
        OkHttpClient client = new OkHttpClient();
        okhttp3.Request request = new okhttp3.Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);

    }
}
