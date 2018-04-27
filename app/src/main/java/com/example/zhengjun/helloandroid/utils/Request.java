package com.example.zhengjun.helloandroid.utils;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    public AsyncHttpClient client = new AsyncHttpClient();

    private static final String HOST = "http://api.reactorlive.com/";

    private static Context context;

    private boolean save = true;

    public Request(Context context){
        this.context = context;
    }

    public static String getHost(){
        return HOST;
    }

    public void get(final String url, final Response response, final boolean cache){
        final Storage s = new Storage(context);

        if( cache && !Helper.getInstance().getNetWorkStatus(context)){
            try {
                JSONObject object = s.getJson(url);

                if( object.has("code") ) {
                    response.onSuccess(200, object);
                    response.onFinally();

                    return;
                }

            }catch (JSONException e){

            }
        }

        client.addHeader("token", getToken());
        client.get(getAbsoluteUrl(url), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                response.onSuccess(statusCode, jsonObject);
                response.onFinally();
                if (save)
                    s.setJson(url, jsonObject);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                response.onFailure(statusCode, jsonObject, error);
                response.onFinally();
            }
        });
    }

    public void post(String url, RequestParams params, final Response response){
        client.addHeader("token", getToken());
        client.post(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                response.onSuccess(statusCode, jsonObject);
                response.onFinally();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                response.onFailure(statusCode, jsonObject, error);
                response.onFinally();
            }

        });
    }

    public void put(String url, RequestParams params, final Response response){
        client.addHeader("token", getToken());
        client.put(getAbsoluteUrl(url), params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                response.onSuccess(statusCode, jsonObject);
                response.onFinally();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable error, JSONObject jsonObject) {
                response.onFailure(statusCode, jsonObject, error);
                response.onFinally();
            }

        });
    }

    public void delete(String url, RequestParams params, final Response response) {
        client.addHeader("token", getToken());
        Log.e("token", getToken());
        Log.e("params", params.toString());
        client.delete(getAbsoluteUrl(url), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.e("status", statusCode + "");
                Log.e("success", result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String result = new String(responseBody);
                Log.e("status", statusCode + "");
                Log.e("success", result);
            }
        });
    }

    public String getToken(){
        Storage s = new Storage(context);

        String token = s.get("token", "");
        if( token == null || token.length() < 1 ){
            String uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            token = Helper.getInstance().md5(uuid);
        }

        return token;
    }

    public static String getAbsoluteUrl(String url) {
        return HOST + url;
    }

    public void setSave(boolean save){
        this.save = save;
    }
}
