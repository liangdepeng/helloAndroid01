package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.popwindow.MyDialog;
import com.example.zhengjun.helloandroid.popwindow.SelectSexPopwindow;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tony on 2018/4/25.
 */

public class ChangeMineInfoActivity extends Activity implements View.OnClickListener {
    private RelativeLayout set_nickname;
    private RelativeLayout set_sex;
    private RelativeLayout set_signature;
    private TextView nickname;
    private TextView sex;
    private TextView signature;
    private SelectSexPopwindow selectsex;
    private ImageView back;
    private Button exit;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_mineinfo);
        init();
    }
    public void init(){
        exit = (Button)findViewById(R.id.exit);
        exit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.change_info_back);
        back.setOnClickListener(this);
        nickname = (TextView)findViewById(R.id.nickname);
        sex = (TextView)findViewById(R.id.sex);
        signature=(TextView)findViewById(R.id.signature);
        set_nickname = (RelativeLayout)findViewById(R.id.set_nickname);
        set_sex = (RelativeLayout )findViewById(R.id.set_sex);
        set_signature = (RelativeLayout)findViewById(R.id.set_signature);
        set_nickname.setOnClickListener(this);
        set_sex.setOnClickListener(this);
        set_signature.setOnClickListener(this);
        if (getIntent().getExtras().getString("sex").equals("0")){
            sex.setText("保密");

        }else if(getIntent().getExtras().getString("sex").equals("1")){
            sex.setText("男");
        }else if(getIntent().getExtras().getString("sex").equals("2")){
            sex.setText("女");
        }
        nickname.setText(getIntent().getExtras().getString("nickname"));
        signature.setText(getIntent().getExtras().getString("sign"));
        signature.setText("");
        selectsex = new SelectSexPopwindow(ChangeMineInfoActivity.this, new
                SelectSexPopwindow.PopwindowListener() {
                    @Override
                    public void back(String result) {
                        sex.setText(result);
                        if (result.equals(" 保密"))
                            sendData("sex", "0");
                        else if (result.equals(" 男"))
                            sendData("sex", "1");
                        else if (result.equals(" 女"))
                            sendData("sex", "2");
                    }//    666 !
                });
    }
    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.set_nickname:
            MyDialog myDialog = new MyDialog(this, "编辑昵称", new MyDialog.OnCustomDialogListener() {
                @Override
                public void back(String name) {
                    if(name.length()>0&&!name.equals("null")){
                        nickname.setText(name);
                    }
                    sendData("nickname",name);
                }
            });
            myDialog.show();
            break;
            case R.id.set_sex:
                selectsex.showAtLocation(ChangeMineInfoActivity.this.findViewById(R.id.main),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.set_signature:
//实现修改个性签名功能

                MyDialog myDialog2 = new MyDialog(this, "编辑个性签名", new MyDialog.OnCustomDialogListener() {
                    @Override
                    public void back(String sign) {
                        if(signature.length()>0&&!signature.equals("null")){
                            signature.setText(sign);
                        }
                        sendData("signature",sign);
                    }
                });
                myDialog2.show();


                break;
            case R.id.change_info_back:
                finish();
                break;
            case R.id.exit:
                exit();
            default:
                break;
        }
    }

    private void sendData(String type, String data) {
        RequestParams params = new RequestParams();
        params.add(type, data);
        params.add("edit", "1");
        BaseClient.put("member", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
// TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                try {
                    Toast.makeText(ChangeMineInfoActivity.this,
                            response.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  Throwable throwable, JSONObject errorResponse) {
// TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e(" onf ailure", errorResponse.toString());
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  String responseString, Throwable throwable) {
// TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("O F ", String.valueOf(statusCode) + "" + String.valueOf(throwable));
            }
        });
    }

    private void exit() {
        BaseClient.post("member/logout", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
// TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ChangeMineInfoActivity.this, " 退出当前帐号成功",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
// TODO Auto-generated method stub
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("exit String", " " + statusCode + " " + responseString + " " + String.valueOf(throwable));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
// TODO Auto-generated method stub
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("String code ", statusCode + " " + errorResponse.toString());
            }
        });
    }

}

