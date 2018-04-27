package com.example.zhengjun.helloandroid.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;


/**
 * Created by Hawking on 2015/11/8.
 */
public abstract class WebAppInterface {

    public Context mContext;
    private WebView mWebview;
    public Request mRequest;

    private static final int POST   = 1;
    private static final int PUT    = 2;
    private static final int DELETE = 3;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c, WebView webView) {
        mContext = c;
        mRequest = new Request(c);
        mWebview = webView;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void goActivity(String className, String params){
        try{
            Intent intent = new Intent(mContext, Class.forName(className));
            intent.putExtra("params", params);
            mContext.startActivity(intent);
        }catch (ClassNotFoundException e){
            Log.e("error", e.toString());
        }
    }

    @JavascriptInterface
    public abstract String getString(String name);

    @JavascriptInterface
    public int getWidth(){
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    @JavascriptInterface
    public void setStorage(String key, String value){
        Storage s = new Storage(mContext);
        s.set(key, value);
    }

    @JavascriptInterface
    public void camera( String callback ){

    }

    @JavascriptInterface
    public void openInBrowser( String url ){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public int getHeight(){
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    @JavascriptInterface
    public void doGet(String url, final String callback, boolean cache){
        mRequest.setSave(true);
        mRequest.get(url, new Response() {
            @Override
            public void onSuccess(int statusCode, final JSONObject jsonObject) {
                mWebview.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.loadUrl("javascript: " + callback + "(" + jsonObject + ");");
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, final JSONObject jsonObject, Throwable error) {
                mWebview.post(new Runnable() {
                    @Override
                    public void run() {
                        mWebview.loadUrl("javascript: " + callback + "(" + jsonObject + ");");
                    }
                });
            }
        }, cache);
    }

    @JavascriptInterface
    public void doRequest(String url, int method, final String paramters, final String callback){
        try {
            JSONObject object = new JSONObject(paramters);

            RequestParams params = new RequestParams();

            Iterator it = object.keys();

            while(it.hasNext()){
                String key = (String) it.next();

                if( key.equals("image[]") || key.equals("audio[]")){
                    File file = new File(object.get(key).toString());
                    if( file.exists() && file.length() > 0)
                        params.put(key, file);
                }
                else
                    params.put(key, object.get(key));
            }

            Response response = new Response() {
                @Override
                public void onSuccess(int statusCode, final JSONObject jsonObject) {
                    mWebview.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebview.loadUrl("javascript: " + callback + "(" + jsonObject + ", " + paramters + ");");
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, final JSONObject jsonObject, Throwable error) {
                    mWebview.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebview.loadUrl("javascript: " + callback + "(" + jsonObject + ", " + paramters + ");");
                        }
                    });
                }

                public void onSuccess(int statusCode, final String result) {
                    Log.e("rs", result);
                    mWebview.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebview.loadUrl("javascript: " + callback + "(" + result + ", " + paramters + ");");
                        }
                    });
                }

                public void onFailure(int statusCode, final String result, Throwable error) {
                    Log.e("rs", result);
                    mWebview.post(new Runnable() {
                        @Override
                        public void run() {
                            mWebview.loadUrl("javascript: " + callback + "(" + result + ", " + paramters + ");");
                        }
                    });
                }
            };

            switch (method){
                case POST:   mRequest.post(url, params, response);break;
                case PUT:    mRequest.put(url, params, response);break;
                case DELETE: mRequest.delete(url, params, response);break;
                default: break;
            }

        }catch(JSONException e){
            Log.e("requst failed", e.toString());
        }catch (FileNotFoundException e){
            Log.e("file not found", e.toString());
        }
    }

    @JavascriptInterface
    public String getToken(){
        return mRequest.getToken();
    }

    @JavascriptInterface
    public String getHost(){
        return mRequest.getHost();
    }

    @JavascriptInterface
    public String getMember(){
        Storage s = new Storage(mContext);
        return s.get("member");
    }
}
