package com.example.zhengjun.helloandroid.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Administrator on 2018/4/16.
 */

public class GetTokenClient {
    private static final String BASE_URL =
            "http://api.reactorlive.com/auth/token?appid=cb_7jcelkr9yr82b&secret=b4700f27855f4ee1576bae09c14a7682b32bc84c";
    private static AsyncHttpClient client = new AsyncHttpClient();
    public static void get(RequestParams params, AsyncHttpResponseHandler
            responseHandler, String device) {
        client.addHeader("token", device);
        client.get(BASE_URL, params, responseHandler);
    }
    public static void post(RequestParams params, AsyncHttpResponseHandler
            responseHandler) {
        client.post(BASE_URL, params, responseHandler);
    }
    public static void put(RequestParams params,AsyncHttpResponseHandler
            responseHandler){
        client.put(BASE_URL, params, responseHandler);
    }
}
