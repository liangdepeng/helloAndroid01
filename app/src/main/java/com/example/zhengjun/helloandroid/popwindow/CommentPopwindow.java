package com.example.zhengjun.helloandroid.popwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.utils.FastBlur;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

@SuppressLint("ResourceAsColor")
public class CommentPopwindow extends PopupWindow implements OnClickListener {
	private View mMenuView;
	private IWXAPI wxapi;

	private Button login;
	private ImageView close;
	private Context mContext;
	private FrameLayout fara ;
	@SuppressLint("ResourceAsColor")
	public CommentPopwindow(Context context,Bitmap bmp) {
		super(context);
		mContext=context;
		wxapi = WXAPIFactory.createWXAPI(context, "wx332f009452b3e86e");
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.wechat_login_activity, null);
		login = (Button) mMenuView.findViewById(R.id.login);
		close = (ImageView) mMenuView.findViewById(R.id.close_wechat_login);
		fara = (FrameLayout)mMenuView.findViewById(R.id.login_pop);
		login.setOnClickListener(this);
		close.setOnClickListener(this);
		// cancle.setOnClickListener(this);
		// 设置按钮监听
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		// this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xdc000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		blur(bmp, fara);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.close_wechat_login:
			dismiss();
			break;
		case R.id.login:
			final SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			wxapi.sendReq(req);
			this.dismiss();
			break;
		default:
			break;
		}
	}
	private void blur(Bitmap bkg, View view) {  
	    long startMs = System.currentTimeMillis();  
	    float scaleFactor = 5;//图片缩放比例；  
	    float radius =40;//模糊程度  
	    Bitmap overlay = Bitmap.createBitmap(  
	            (int) (mContext.getSharedPreferences("width", Activity.MODE_PRIVATE).getInt("width", 720)/ scaleFactor),  
	            (int) (mContext.getSharedPreferences("width", Activity.MODE_PRIVATE).getInt("height", 1280) / scaleFactor),  
	            Bitmap.Config.ARGB_8888);  
	    Canvas canvas = new Canvas(overlay);  
	    canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()/ scaleFactor);  
	    canvas.scale(1 / scaleFactor, 1 / scaleFactor);  
	    Paint paint = new Paint();  
	    paint.setFlags(Paint.FILTER_BITMAP_FLAG);  
	    canvas.drawBitmap(bkg, 0, 0, paint);  
	    overlay = FastBlur.doBlur(overlay, (int) radius, false);
	    view.setBackground(new BitmapDrawable(mContext.getResources(), overlay));  
	    /** 
	     * 打印高斯模糊处理时间，如果时间大约16ms，用户就能感到到卡顿，时间越长卡顿越明显，如果对模糊完图片要求不高，可是将scaleFactor设置大一些。 
	     */  
	  //  Log.e("jerome", "blur time:" + (System.currentTimeMillis() - startMs));  
	}  
}
