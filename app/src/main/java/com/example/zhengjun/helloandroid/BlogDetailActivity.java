package com.example.zhengjun.helloandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhengjun.helloandroid.popwindow.WxSharePopwindow;
import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class BlogDetailActivity extends Activity implements OnClickListener {
    private CircleImageView blog_user_photo;
    private TextView blog_content;
    private HorizontalScrollView blog_image_container;
    private ImageView[] blogimages = new ImageView[6];
    private ImageView praise_label;
    private TextView praise_info;
    private TextView publish_time;
    private TextView blog_user_name;
    private LinearLayout blog_praise_container;
    private String id;
    private String nickname;
    private ImageView blogDetailBack;
    private ImageView blog_share;
    WxSharePopwindow wxSharePopwindow;
    private String intro = "";
    private String image = null;
    private ACache mACache;
    private String[] img_path = new String[6];
    private int is_praise = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);
        init();
        getData(id);
    }

    public void init() {
        blog_share = (ImageView) findViewById(R.id.blog_share);
        blog_share.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String url = "http://admin.reactorlive.com//Home/Share/dairy/id/"
                        + id + ".html";
                wxSharePopwindow = new WxSharePopwindow(
                        BlogDetailActivity.this, url, nickname + "发表了日志", intro, image);
                wxSharePopwindow.showAtLocation(BlogDetailActivity.this
                                .findViewById(R.id.blog_all_content),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        blogDetailBack = (ImageView) findViewById(R.id.blog_detail_back);
        blogDetailBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        id = getIntent().getExtras().getString("blog_id");
        blog_content = (TextView) findViewById(R.id.blog_detail);
        blog_image_container = (HorizontalScrollView) findViewById(R.id.blog_image_container);
        blog_user_photo = (CircleImageView) findViewById(R.id.blog_user_image);
        publish_time = (TextView) findViewById(R.id.blog_publish_time);
        blog_user_name = (TextView) findViewById(R.id.blog_user_name);
        blogimages[0] = (ImageView) findViewById(R.id.img1);
        blogimages[0].setOnClickListener(this);
        blogimages[1] = (ImageView) findViewById(R.id.img2);
        blogimages[1].setOnClickListener(this);
        blogimages[2] = (ImageView) findViewById(R.id.img3);
        blogimages[2].setOnClickListener(this);
        blogimages[3] = (ImageView) findViewById(R.id.img4);
        blogimages[3].setOnClickListener(this);
        blogimages[4] = (ImageView) findViewById(R.id.img5);
        blogimages[4].setOnClickListener(this);
        blogimages[5] = (ImageView) findViewById(R.id.img6);
        blogimages[5].setOnClickListener(this);
        praise_label = (ImageView) findViewById(R.id.blog_praise_label);
        blog_praise_container = (LinearLayout) findViewById(R.id.blog_praise_linearlayout);
        blog_praise_container.setOnClickListener(this);
        praise_info = (TextView) findViewById(R.id.blog_prise_info);
        mACache = ACache.get(this);
        if (!PublicUtils.isNetworkAvailable(this)) {
            if (mACache.getAsJSONObject("BlogDetail" + id) != null) {
                JSONObject response = mACache
                        .getAsJSONObject("BlogDetail" + id);
                try {

                    JSONObject value = response.getJSONObject("value");
                    getMemberInfo(value.getString("member_id"));
                    nickname = value.getJSONObject("member").getString("nickname");
                    blog_content.setText(value.getString("content"));
                    intro = value.getString("content");
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yy.MM.dd hh:mm");
                    publish_time.setText(format.format(new Date(value
                            .getLong("create_time") * 1000)));
                    praise_info.setText(value.getString("praise_number")
                            + "觉得很赞");
                    is_praise = value.getInt("isPraise");
                    int imageNumber = 0;
                    imageNumber = value.getInt("image_number");
                    if (imageNumber > 0) {
                        blog_image_container.setVisibility(View.VISIBLE);
                        if (value.getString("image1").length() > 0
                                && !value.getString("image1").equals("null")) {
                            blogimages[0].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image1")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[0]);
                            image = value.getString("image1");
                            img_path[0] = value.getString("image1");

                        }
                        if (value.getString("image2").length() > 0
                                && !value.getString("image2").equals("null")) {
                            blogimages[1].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image2")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[1]);
                            img_path[1] = value.getString("image2");
                        }
                        if (value.getString("image3").length() > 0
                                && !value.getString("image3").equals("null")) {
                            blogimages[2].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image3")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[2]);
                            img_path[2] = value.getString("image3");
                        }
                        if (value.getString("image4").length() > 0
                                && !value.getString("image4").equals("null")) {
                            blogimages[3].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image4")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[3]);
                            img_path[3] = value.getString("image4");
                        }
                        if (value.getString("image5").length() > 0
                                && !value.getString("image5").equals("null")) {
                            blogimages[4].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image5")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[4]);
                            img_path[4] = value.getString("image5");
                        }
                        if (value.getString("image6").length() > 0
                                && !value.getString("image6").equals("null")) {
                            blogimages[5].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image6")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[5]);
                            img_path[5] = value.getString("image6");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private void getData(final String id) {
        BaseClient.get("Logs?id=" + id, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("log detail", response.toString());
                mACache.put("BlogDetail" + id, response);
                try {
                    JSONObject value = response.getJSONObject("value");
                    getMemberInfo(value.getString("member_id"));
                    nickname = value.getJSONObject("member").getString("nickname");
                    blog_content.setText(value.getString("content"));
                    intro = value.getString("content");
                    SimpleDateFormat format = new SimpleDateFormat(
                            "yy.MM.dd hh:mm");
                    publish_time.setText(format.format(new Date(value
                            .getLong("create_time") * 1000)));
                    praise_info.setText(value.getString("praise_number")
                            + "觉得很赞");
                    int imageNumber = 0;
                    is_praise = value.getInt("isPraise");
                    if (is_praise == 1) {
                        praise_label
                                .setImageResource(R.drawable.prise_active_detail);
                    } else {
                        praise_label.setImageResource(R.drawable.prise_detail);
                    }
                    imageNumber = value.getInt("image_number");
                    if (imageNumber > 0) {
                        blog_image_container.setVisibility(View.VISIBLE);
                        if (value.getString("image1").length() > 0
                                && !value.getString("image1").equals("null")) {
                            blogimages[0].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image1")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[0]);
                            image = value.getString("image1");
                            img_path[0] = value.getString("image1");
                        }
                        if (value.getString("image2").length() > 0
                                && !value.getString("image2").equals("null")) {
                            blogimages[1].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image2")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[1]);
                            img_path[1] = value.getString("image2");
                        }
                        if (value.getString("image3").length() > 0
                                && !value.getString("image3").equals("null")) {
                            blogimages[2].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image3")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[2]);
                            img_path[2] = value.getString("image3");
                        }
                        if (value.getString("image4").length() > 0
                                && !value.getString("image4").equals("null")) {
                            blogimages[3].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image4")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[3]);
                            img_path[3] = value.getString("image4");
                        }
                        if (value.getString("image5").length() > 0
                                && !value.getString("image5").equals("null")) {
                            blogimages[4].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image5")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[4]);
                            img_path[4] = value.getString("image5");
                        }
                        if (value.getString("image6").length() > 0
                                && !value.getString("image6").equals("null")) {
                            blogimages[5].setVisibility(View.VISIBLE);
                            ImageLoader.getInstance()
                                    .displayImage(
                                            "http://api.reactorlive.com/media/"
                                                    + value.getString("image6")
                                                    + "/w/420/h/280/m/0",
                                            blogimages[5]);
                            img_path[5] = value.getString("image6");
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        });
    }

    private void getMemberInfo(String member_id) {
        BaseClient.get("member?id=" + member_id, null,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers,
                                          JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONObject value = response.getJSONObject("value");
                            blog_user_name.setText(value.getString("nickname"));
                            ImageLoader.getInstance().displayImage(
                                    value.getString("avatar"), blog_user_photo);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers,
                                          Throwable throwable, JSONObject errorResponse) {
                        // TODO Auto-generated method stub
                        super.onFailure(statusCode, headers, throwable,
                                errorResponse);
                    }

                });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img1:
                Log.e("image_path", img_path[0]);
                Intent img1 = new Intent(this, ImageActivity.class);
                img1.putExtra("image_path", img_path[0]);
                startActivity(img1);
                break;
            case R.id.img2:
                Intent img2 = new Intent(this, ImageActivity.class);
                img2.putExtra("image_path", img_path[1]);
                startActivity(img2);
                break;
            case R.id.img3:
                Intent img3 = new Intent(this, ImageActivity.class);
                img3.putExtra("image_path", img_path[2]);
                startActivity(img3);
                break;
            case R.id.img4:
                Intent img4 = new Intent(this, ImageActivity.class);
                img4.putExtra("image_path", img_path[3]);
                startActivity(img4);
                break;
            case R.id.img5:
                Intent img5 = new Intent(this, ImageActivity.class);
                img5.putExtra("image_path", img_path[4]);
                startActivity(img5);
                break;
            case R.id.img6:
                Intent img6 = new Intent(this, ImageActivity.class);
                img6.putExtra("image_path", img_path[5]);
                startActivity(img6);
                break;
            case R.id.blog_praise_linearlayout:
                switch (is_praise) {
                    case 0:
                        RequestParams params1 = new RequestParams();
                        params1.add("id", id);
                        params1.add("like", "1");
                        params1.add("status", "1");
                        BaseClient.put("Logs", params1, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  JSONObject response) {
                                // TODO Auto-generated method stub
                                super.onSuccess(statusCode, headers, response);
                                is_praise = 1;
                                praise_label
                                        .setImageResource(R.drawable.prise_active_detail);
                                praise_info.setText(String.valueOf(Integer
                                        .valueOf(praise_info.getText().toString()
                                                .substring(0, 1)) + 1) + "人觉得很赞");
                                Log.e("praise0", praise_info.getText() + "");
                            }
                        });
                        break;
                    case 1:
                        RequestParams params2 = new RequestParams();
                        params2.add("id", id);
                        params2.add("like", "1");
                        params2.add("status", "0");
                        BaseClient.put("Logs", params2, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers,
                                                  JSONObject response) {
                                // TODO Auto-generated method stub
                                super.onSuccess(statusCode, headers, response);

                                is_praise = 0;
                                praise_label.setImageResource(R.drawable.prise_detail);
                                praise_info.setText(String.valueOf(Integer
                                        .valueOf(praise_info.getText().toString()
                                                .substring(0, 1)) - 1) + "人觉得很赞");
                                Log.e("praise1", praise_info.getText() + "");
                            }
                        });
                        break;
                    default:
                        break;
                }

            default:
                break;
        }
    }

}
