package com.example.zhengjun.helloandroid.wxapi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    String code = "";
    String access_token = "";
    String openid = "";
    String expires_in;
    String refresh_token;
    String unionid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, getString(R.string.app_id), false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Log.e("request", baseReq.toString());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int result = 0;

        Log.e("errCode", baseResp.errCode + "");
        Log.e("resType", baseResp.getType() + "");
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                switch (baseResp.getType()) {
                    case 1:
                        code = ((SendAuth.Resp) baseResp).code;
                        Log.e("code", code);
                        getWXToken();
                        break;
                    case 2:
                        Toast.makeText(WXEntryActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default: finish();
                }
                result = 1;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = 2;
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = 3;
                finish();
                break;
            default:
                result = 4;
                finish();
                break;
        }
    }

    public void getWXToken(){
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + getString(R.string.app_id) +
                "&secret=" + getString(R.string.app_secret) +
                "&code=" + code +
                "&grant_type=authorization_code";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("wxToken", new String(responseBody));
                try {
                    JSONObject object = new JSONObject(new String(responseBody));

                    expires_in    = object.getString("expires_in");
                    refresh_token = object.getString("refresh_token");
                    unionid       = object.getString("unionid");
                    access_token  = object.getString("access_token");
                    openid = object.getString("openid");
                    getDetail(access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("body", new String(responseBody));
                finish();
            }
        });
    }

    public void getDetail( String at, final String openid){
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + at + "&openid=" + openid;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("wxDetail", new String(responseBody));
                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));

                    RequestParams params = new RequestParams();
                    params.add("sex", jsonObject.getString("sex"));
                    params.add("nickname", jsonObject.getString("nickname"));
                    params.add("unionid", jsonObject.getString("unionid"));
                    params.add("privilege", jsonObject.getString("privilege"));
                    params.add("province", jsonObject.getString("province"));
                    params.add("language", jsonObject.getString("language"));
                    params.add("headimgurl", jsonObject.getString("headimgurl"));
                    params.add("country", jsonObject.getString("country"));
                    params.add("city", jsonObject.getString("city"));
                    params.add("openid", openid);
                    doLogin(params);
                } catch (JSONException e) {
                    Log.e("json e", e.toString());
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("login", new String(responseBody));
                finish();
            }
        });
    }

    public void doLogin(RequestParams params) {
        params.add("token", getSharedPreferences("token", Activity.MODE_PRIVATE).getString("token", "null"));;
        BaseClient.post("member/login", params, new JsonHttpResponseHandler() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        // If the response is JSONObject instead of expected
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        // TODO Auto-generated method stub
                        super.onFailure(statusCode, headers, throwable,
                                errorResponse);
                        Log.e("onFailure", errorResponse.toString());
                    }
                });
    }
}