package com.example.zhengjun.helloandroid;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.fragment.FestivalFragment;
import com.shizhefei.fragment.FirstLayerFragment;
import com.shizhefei.fragment.MineFragment;
import com.shizhefei.fragment.SortFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.viewpager.SViewPager;

/**
 * Created by Administrator on 2018/4/13.
 */

public class TabMainActivity extends FragmentActivity {
    private IndicatorViewPager indicatorViewPager;



    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_tabmain);
        SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
        Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
    }


    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = { "首页", "节展", "分类", "我的" };
        private int[] tabIcons = { R.drawable.maintab_1_selector,
                R.drawable.maintab_2_selector, R.drawable.maintab_3_selector,
                R.drawable.maintab_4_selector };
        private LayoutInflater inflater;
        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }
        @Override
        public int getCount() {
            return tabNames.length;
        }
        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            if (convertView == null) {
                convertView = (TextView) inflater.inflate(R.layout.tab_main,
                        container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            Drawable d = getResources().getDrawable(tabIcons[position]);
            d.setBounds(0, 0, 46, 46);
            textView.setCompoundDrawables(null, d, null, null);
            return textView;
        }
        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FirstLayerFragment();
                    break;
                case 1:
                    fragment = new FestivalFragment();
                    break;
                case 2:
                    fragment = new SortFragment();
                    break;
                case 3:
                    fragment = new MineFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }
    }
}
