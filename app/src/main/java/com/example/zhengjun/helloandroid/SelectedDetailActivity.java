package com.example.zhengjun.helloandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.example.zhengjun.helloandroid.view.MyScrollView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.example.zhengjun.helloandroid.view.MyScrollView.OnScrollListener;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/9.
 */

public class SelectedDetailActivity extends Activity {

    private ImageView big_img;
    private TextView brief_title;
    private TextView publish_time;
    private TextView collection_info;
    private WebView artical_detail;
    private MyScrollView content;
    private ImageView back;
    private ImageView collect_img;
    private RelativeLayout relativeLayout;
    private ImageView share_image;
    private RelativeLayout header;
    private TextView select_title;
    private String id = "";
    private int width = 0;
    private ACache mACache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_detail);
        id = getIntent().getExtras().getString("selected_id");

        init();
        try {
            getData();
        } catch (JSONException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void init() {
        share_image = (ImageView) findViewById(R.id.select_share);
        select_title=(TextView)findViewById(R.id.select_title);
        relativeLayout = (RelativeLayout) findViewById(R.id.select_detail_collect);
        collect_img = (ImageView) findViewById(R.id.collect_img);
        content = (MyScrollView) findViewById(R.id.select_detail_content);
        back = (ImageView) findViewById(R.id.select_detail_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
        big_img = (ImageView) findViewById(R.id.big_img);
        brief_title = (TextView) findViewById(R.id.brief_title);
        publish_time = (TextView) findViewById(R.id.public_time);
        collection_info = (TextView) findViewById(R.id.collection_info);
        artical_detail = (WebView) findViewById(R.id.artical_detail);
        header=(RelativeLayout)findViewById(R.id.header);
        mACache = ACache.get(this);

        content.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
// TODO Auto-generated method stub
// 当滚动距离小于等于10的时候，修改返回和分享按钮图片，隐藏标题，将背景设为透明
// 当滚动距离大于10的时候，显示标题，背景颜色为白色，修改返回和分享按钮图片，修改背景透明度
                Log.e("scrollY", scrollY+"");
                if (scrollY > 10) {
                    int alpha = 5 * scrollY;
                    if (alpha > 255) {
                        alpha = 255;
                    }
                    select_title.setVisibility(View.VISIBLE);
                    header.setBackgroundColor(Color.WHITE);
                    back.setImageResource(R.drawable.topbar_back_black);
                    share_image.setImageResource(R.drawable.topbar_share_black);
                    header.getBackground().setAlpha(alpha);
                } else if (scrollY <= 10) {
                    share_image.setImageResource(R.drawable.share_topbar);
                    back.setImageResource(R.drawable.back_topbar);
                    select_title.setVisibility(View.GONE);
                    header.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });


        if (!PublicUtils.isNetworkAvailable(this)) {
            if (mACache.getAsJSONObject("SelectDetail" + id) != null) {
                JSONObject response = mACache.getAsJSONObject("SelectDetail" + id);
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject value = response.getJSONObject("value");
                        brief_title.setText(value.getString("title"));
                        select_title.setText(value.getString("title"));
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("yyyy.MM.dd HH:mm");
                        publish_time.setText(formatter.format(
                                new Date(value.getLong("create_time") * 1000)));
                        collection_info.setText(value.getString("collection_number"));
                        String htmlString = value.getString("content").
                                replaceAll("image-reactor.pachira.cc", "image.reactorlive.com");
                        artical_detail.loadDataWithBaseURL("", htmlString, "text/html",
                                "utf-8", "");
                        String url = "http://api.reactorlive.com/media/" +
                                value.getString("image") + "/w/" + width + "/h/" + width * 0.6 + "/m/0";
                        ImageLoader.getInstance().displayImage(url, big_img);
                        if (value.getInt("is_collect") == 1) {
                            collect_img.setImageResource(R.drawable.collecting_active);
                        } else if (value.getInt("is_collect") == 0) {
                            collect_img.setImageResource(R.drawable.collecting_normal);
                        }
                    }
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public void getData() throws JSONException {
        BaseClient.get("Choice?id=" + id, null, new JsonHttpResponseHandler() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
// If the response is JSONObject instead of expected JSONArra
                Log.e("Success detail", response.toString());
                mACache.put("SelectDetail" + id, response);
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject value = response.getJSONObject("value");
                        brief_title.setText(value.getString("title"));
                        select_title.setText(value.getString("title"));
                        SimpleDateFormat formatter =

                                new SimpleDateFormat("yyyy.MM.dd HH:mm");
                        publish_time.setText(formatter.format(
                                new Date(value.getLong("create_time") * 1000)));
                        collection_info.setText(value.getString("collection_number"));
                        String htmlString = value.getString("content").
                                replaceAll("image-reactor.pachira.cc", "image.reactorlive.com");
                        artical_detail.loadDataWithBaseURL("", htmlString, "text/html",
                                "utf-8", "");
                        String url = "http://api.reactorlive.com/media/" +
                                value.getString("image") + "/w/" + width + "/h/" + width * 0.6 + "/m/0";
                        ImageLoader.getInstance().displayImage(url, big_img);
                        if (value.getInt("is_collect") == 1) {
                            collect_img.setImageResource(R.drawable.collecting_active);
                        } else if (value.getInt("is_collect") == 0) {
                            collect_img.setImageResource(R.drawable.collecting_normal);
                        }
                    }
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}
