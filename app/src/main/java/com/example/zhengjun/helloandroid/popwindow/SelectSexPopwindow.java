package com.example.zhengjun.helloandroid.popwindow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.zhengjun.helloandroid.R;

@SuppressLint("ResourceAsColor")
public class SelectSexPopwindow extends PopupWindow implements OnClickListener{  
	  
	public interface PopwindowListener{
		public void back(String result);
	}
	public PopwindowListener popwindowListener;
    private Button man, woman, secret;  
    private View mMenuView;  
  
    @SuppressLint("ResourceAsColor")
	public SelectSexPopwindow(Activity context,PopwindowListener popwindowListener) {  
        super(context);  
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        this.popwindowListener = popwindowListener;
        mMenuView = inflater.inflate(R.layout.popwindow_sex_info, null);
        secret = (Button) mMenuView.findViewById(R.id.secret);  
        man = (Button) mMenuView.findViewById(R.id.man);  
        woman = (Button) mMenuView.findViewById(R.id.woman);  
        //取消按钮  
      
        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        secret.setOnClickListener(this);
        //设置按钮监听  
        
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.FILL_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.WRAP_CONTENT);  
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
		case R.id.man:
			man.setTextColor(R.color.black);
        	woman.setTextColor(R.color.sex_item_unse);
        	secret.setTextColor(R.color.sex_item_unse);
        	SelectSexPopwindow.this.popwindowListener.back(man.getText().toString());
			break;
		case R.id.woman:
			woman.setTextColor(R.color.black);
        	man.setTextColor(R.color.sex_item_unse);
        	secret.setTextColor(R.color.sex_item_unse);
        	SelectSexPopwindow.this.popwindowListener.back(woman.getText().toString());
			break;
		case R.id.secret:
			secret.setTextColor(R.color.black);
        	man.setTextColor(R.color.sex_item_unse);
        	woman.setTextColor(R.color.sex_item_unse);
        	SelectSexPopwindow.this.popwindowListener.back(secret.getText().toString());
			break;
		default:
			break;
		}
	}  
	
}  