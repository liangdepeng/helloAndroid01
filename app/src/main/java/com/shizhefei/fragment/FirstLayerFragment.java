package com.shizhefei.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhengjun.helloandroid.R;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.Date;

public class FirstLayerFragment extends LazyFragment {

	private IndicatorViewPager indicatorViewPager;
	private TextView title;
	private ImageView right_img;
	private Button left_button;

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_first_layer);
		init();
	}
	public void init() {
		Resources res = getResources();
		title = (TextView) findViewById(R.id.title);
		title.setText("嬉戏测试版");
		right_img = (ImageView) findViewById(R.id.setting);
		left_button = (Button) findViewById(R.id.canlendar);
		left_button.setText(String.valueOf(new Date().getDate()));
		ViewPager viewPager = (ViewPager) findViewById(R.id.fragment_tabmain_viewPager);
		Indicator indicator = (Indicator) findViewById(R.id.fragment_tabmain_indicator);
		indicator.setScrollBar(new ColorBar(getApplicationContext(),
				getResources().getColor(R.color.pink), 5));
		int selectColor = res.getColor(R.color.pink);
		int unSelectColor = res.getColor(R.color.tab_text_color);
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColor(selectColor, unSelectColor).setSize(17, 17));
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
// 注意这里的 FragmentManager 是 getChildFragmentManager(); 因为是在Fragment里面
// 而在activity里面用FragmentManager 是getSupportFragmentManager()
		indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
	}
	private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
		private LayoutInflater inflater;
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			inflater = LayoutInflater.from(getApplicationContext());
		}
		@Override
		public int getCount() {
			return 2;
		}
		@Override
		public View getViewForTab(int position, View convertView,
								  ViewGroup container) {
			switch (position) {
				case 0:
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.tab_top, container,
								false);
					}
					((TextView)convertView).setText("精选");
					break;
				case 1:
					if (convertView == null) {
						convertView = inflater.inflate(R.layout.tab_top, container,
								false);
					}
					((TextView)convertView).setText("我的艺术圈");
					break;
				default:
					break;
			}
			return convertView;
		}
		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment fragment = null;
			switch (position) {
				case 0:
					fragment = new SelectedListFragment();
					break;
				case 1:
					fragment = new ArtCircleFragment();
					break;
				default:
					break;
			}
			return fragment;
		}
	}
}
