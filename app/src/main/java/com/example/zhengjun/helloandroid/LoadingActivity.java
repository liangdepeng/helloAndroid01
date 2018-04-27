package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.zhengjun.helloandroid.utils.GetTokenClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingActivity extends FragmentActivity {
	
    private Timer timer;
    
    final Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = new Intent(LoadingActivity.this,TabMainActivity.class);
                    startActivity(intent);
                    timer.cancel();
                    finish();

                    break;
            }
            super.handleMessage(msg);
        }
    };
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);
        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID); //获取设备唯一识别码
        getToken(md5(android_id)); //对识别码进行md5编码，生成客户端token，并发送到服务端
        timer = new Timer(true);
        timer.schedule(task,2000);
    }
    
    TimerTask task = new TimerTask(){
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    public void getToken(String device_id){
        GetTokenClient.get(null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
// TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("success", response.toString());
                try {
                    JSONObject value = response.getJSONObject("value");
                    String token = value.getString("token");
                    SharedPreferences sharedPreferences = getSharedPreferences("token",
                            Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.commit(); //将返回的token保存到本地的SharedPreference里
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONArray response) {
// TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
            }
        }, device_id);
    }

    private static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}

