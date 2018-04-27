package com.example.zhengjun.helloandroid.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("ResourceAsColor")
public class WxSharePopwindow extends PopupWindow implements OnClickListener{  
    private Button to_f, to_m_f, cancle;  
    private View mMenuView;  

    private String url = "";
    private String title = "";
    private String intro="";
    private String image;
    private Context context;
    @SuppressLint("ResourceAsColor")
	public WxSharePopwindow(Context context,String url,String title,String intro,String image) {
        super(context);
        this.context = context;
        this.image = image;
        this.url = url;
        this.title = title;
        this.intro = intro;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        mMenuView = inflater.inflate(R.layout.popwindow_wx_share, null);
        cancle = (Button) mMenuView.findViewById(R.id.share_cancle);
        to_f = (Button) mMenuView.findViewById(R.id.to_friends);  
        to_m_f = (Button) mMenuView.findViewById(R.id.to_many_friends);  
        //取消按钮  
        to_f.setOnClickListener(this);
        to_m_f.setOnClickListener(this);
       // cancle.setOnClickListener(this);
        //设置按钮监听  
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        //this.setAnimationStyle(R.style.AnimBottom);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(0xb0000000);  
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
       
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
              
            public boolean onTouch(View v, MotionEvent event) {  
                  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
  
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.share_cancle:
			dismiss();
			break;
		case R.id.to_friends:
			wechatShare(0);
			dismiss();
			break;
		case R.id.to_many_friends:
			wechatShare(1);
			dismiss();
			break;
		default:
			break;
		}
	}

	private void wechatShare(int flag){
        final IWXAPI wxapi = WXAPIFactory.createWXAPI(context, context.getString(R.string.app_id));
        wxapi.registerApp(context.getString(R.string.app_id));
        if (!wxapi.isWXAppInstalled() ) {
            Toast.makeText(context,"您还没有安装微信", Toast.LENGTH_SHORT).show();
            return;
        }

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		final WXMediaMessage msg = new WXMediaMessage(webpage);

		msg.title = title;
		msg.description = intro;

        final SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
		req.scene = flag==0?SendMessageToWX.Req.WXSceneSession:SendMessageToWX.Req.WXSceneTimeline;
        req.scene = flag;

        final Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

        if(image==null){
            msg.setThumbImage(logo);
            req.message = msg;
            wxapi.sendReq(req);
        }else{
            String uri = "http://api.reactorlive.com/media/" + image + "/w/420/h/280/m/0";

            ImageLoader.getInstance().loadImage(uri, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    msg.setThumbImage(logo);
                    req.message = msg;
                    wxapi.sendReq(req);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    msg.setThumbImage(loadedImage);
                    req.message = msg;
                    wxapi.sendReq(req);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                    msg.setThumbImage(logo);
                    req.message = msg;
                    wxapi.sendReq(req);
                }
            });
        }


	}
}  
