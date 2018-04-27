package com.example.zhengjun.helloandroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * 自定义GridView
 * 
 * @author 等待
 * @class NoScrollGridView.java
 * @time 2014年12月23日 下午11:19:26
 * @QQ 2743569843
 */
public class NoScrollGridView extends GridView {
	public NoScrollGridView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
    } 

    public NoScrollGridView(Context context) { 
        super(context); 
    } 

    public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 

    @Override 
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 

        int expandSpec = MeasureSpec.makeMeasureSpec( 
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST); 
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(ev);
	}
}
