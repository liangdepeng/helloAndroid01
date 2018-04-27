package com.example.zhengjun.helloandroid.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.R;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint({"ResourceAsColor", "NewApi"})
public class MineLoginPopWindow extends PopupWindow implements OnClickListener {
    private View mMenuView;
    private IWXAPI wxapi;
    private FrameLayout fam;
    private Button login;
    private ImageView close;
    private Context mContext;

    @SuppressWarnings("deprecation")
    @SuppressLint("ResourceAsColor")
    public MineLoginPopWindow(Context context, Bitmap bimp) {
        super(context);
        mContext = context;

        wxapi = WXAPIFactory.createWXAPI(context, context.getString(R.string.app_id));
        wxapi.registerApp(context.getString(R.string.app_id));

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.wechat_login_activity, null);
        login = (Button) mMenuView.findViewById(R.id.login);
        close = (ImageView) mMenuView.findViewById(R.id.close_wechat_login);
        login.setOnClickListener(this);
        close.setOnClickListener(this);
        fam = (FrameLayout) mMenuView.findViewById(R.id.login_pop);
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT); // 设置SelectPicPopupWindow弹出窗体的宽
        this.setHeight(LayoutParams.MATCH_PARENT);// 设置SelectPicPopupWindow弹出窗体的高
        this.setFocusable(true);                   // 设置SelectPicPopupWindow弹出窗体可点击

        ColorDrawable dw = new ColorDrawable(0xcc000000);
        this.setBackgroundDrawable(dw);             // 设置SelectPicPopupWindow弹出窗体的背景

        fam.setFocusable(true);
        fam.setFocusableInTouchMode(true);
        fam.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.close_wechat_login:
                dismiss();
                break;
            case R.id.login:
                if(wxapi.isWXAppInstalled() && wxapi.isWXAppSupportAPI() ){
                    final SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    wxapi.sendReq(req);
                    Toast.makeText(mContext, "正在前往微信...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "您还没有安装微信", Toast.LENGTH_SHORT).show();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void dismiss() {
        // TODO Auto-generated method stub
        super.dismiss();
        Log.e("popwindow", "dismiss");
    }


}
