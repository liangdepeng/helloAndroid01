package com.shizhefei.view.indicator.transition;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.shizhefei.view.indicator.Indicator.OnTransitionListener;
import com.shizhefei.view.utils.ColorGradient;

/**
 * 
 * @author LuckyJayce
 *
 */
public class EatOnTransitionTextListener implements OnTransitionListener {
	private float selectSize = -1;
	private float unSelectSize = -1;
	private ColorGradient gradient;
	private float dFontFize = -1;

	private boolean isPxSize = false;
	private Drawable drawable ;
	ViewPositionListener view;
	public EatOnTransitionTextListener(Drawable drawable,ViewPositionListener view) {
		super();
		this.drawable = drawable;
		this.view = view;
	}
	public interface ViewPositionListener{
		public void getPo(int position);
	}
	public EatOnTransitionTextListener(float selectSize, float unSelectSize, int selectColor, int unSelectColor) {
		super();
		setColor(selectColor, unSelectColor);
		setSize(selectSize, unSelectSize);
	}

	public final EatOnTransitionTextListener setSize(float selectSize, float unSelectSize) {
		isPxSize = false;
		this.selectSize = selectSize;
		this.unSelectSize = unSelectSize;
		this.dFontFize = selectSize - unSelectSize;
		return this;
	}

	public final EatOnTransitionTextListener setValueFromRes(Context context, int selectColorId, int unSelectColorId, int selectSizeId,
			int unSelectSizeId) {
		setColorId(context, selectColorId, unSelectColorId);
		setSizeId(context, selectSizeId, unSelectSizeId);
		return this;
	}

	public final EatOnTransitionTextListener setColorId(Context context, int selectColorId, int unSelectColorId) {
		Resources res = context.getResources();
		setColor(res.getColor(selectColorId), res.getColor(unSelectColorId));
		return this;
	}

	public final EatOnTransitionTextListener setSizeId(Context context, int selectSizeId, int unSelectSizeId) {
		Resources res = context.getResources();
		setSize(res.getDimensionPixelSize(selectSizeId), res.getDimensionPixelSize(unSelectSizeId));
		isPxSize = true;
		return this;
	}

	public final EatOnTransitionTextListener setColor(int selectColor, int unSelectColor) {
		gradient = new ColorGradient(unSelectColor, selectColor, 100);
		return this;
	}

	/**
	 * 如果tabItemView 不是目标的TextView，那么你可以重写该方法返回实际要变化的TextView
	 * 
	 * @param tabItemView
	 *            Indicator的每一项的view
	 * @param position
	 *            view在Indicator的位置索引
	 * @return
	 */
	public TextView getTextView(View tabItemView, int position) {
		return (TextView) tabItemView;
	}

	@Override
	public void onTransition(View view, int position, float selectPercent) {
		TextView selectTextView = getTextView(view, position);
		if (gradient != null) {
			selectTextView.setTextColor(gradient.getColor((int) (selectPercent * 100)));
		}
		selectTextView.setCompoundDrawables(null, null, drawable, null);
		this.view.getPo(position);
		if (unSelectSize > 0 && selectSize > 0) {
			if (isPxSize) {
				selectTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, unSelectSize + dFontFize * selectPercent);
			} else {
				selectTextView.setTextSize(unSelectSize + dFontFize * selectPercent);
			}

		}
	}

}
