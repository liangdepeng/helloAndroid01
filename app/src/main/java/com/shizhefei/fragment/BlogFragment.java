package com.shizhefei.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.BlogDetailActivity;
import com.example.zhengjun.helloandroid.ChoosePictureActivity;
import com.example.zhengjun.helloandroid.ImageActivity;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.adapter.MarketAdapter;
import com.example.zhengjun.helloandroid.model.BlogModel;
import com.example.zhengjun.helloandroid.model.MarketModel;
import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.example.zhengjun.helloandroid.utils.TimeDiffer;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.example.zhengjun.helloandroid.view.NoScrollGridView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shizhefei.fragment.LazyFragment;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends LazyFragment {

    private Button new_blog;
    private NoScrollGridView bloglist;
    private List<BlogModel> blogListModels;
    private BlogListAdapter blogadapter;
    private TextView blog_null;
    private ACache aCache;
    private int i = 0;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_blog);
        init();
    }
//	@Override
//	protected void onResumeLazy() {
//		// TODO Auto-generated method stub
//		super.onResumeLazy();
//		if(blogListModels.size()>0){
//			resetViewPagerHeight(0);
//		}else{
//			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) viewPager.getLayoutParams();
//			params.height = height;
//			viewPager.setLayoutParams(params);
//		}
//	}
//

    @Override
    protected void onFragmentStartLazy() {
        // TODO Auto-generated method stub
        super.onFragmentStartLazy();
        getData();
    }

    public void init() {
        blog_null = (TextView) findViewById(R.id.blog_null);
        blogListModels = new ArrayList<BlogModel>();
        bloglist = (NoScrollGridView) findViewById(R.id.fragment_blog_list);
        bloglist.setFocusable(false);
        bloglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), BlogDetailActivity.class);
				intent.putExtra("blog_id", blogListModels.get(position).getCommentID());
				startActivity(intent);
            }
        });

        new_blog = (Button) findViewById(R.id.new_blog);
        new_blog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), ChoosePictureActivity.class);
				intent.putExtra("publish_title", "我的日志");
				intent.putExtra("publish_sort", 0);
				startActivity(intent);
            }
        });
        aCache = ACache.get(getActivity());
        if (!PublicUtils.isNetworkAvailable(getActivity())) {
            if (aCache.getAsJSONObject("blog") != null) {
                JSONObject response = aCache.getAsJSONObject("blog");
                try {
                    JSONObject value = response.getJSONObject("value");
                    JSONArray content = value.getJSONArray("content");
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject object = content.getJSONObject(i);
                        BlogModel bModel = new BlogModel();
                        bModel.setComment(object.getString("content"));
                        bModel.setCommentID(object.getString("id"));
                        List<MarketModel> comImages = new ArrayList<MarketModel>();
                        for (int j = 1; j < 7; j++) {
                            MarketModel coImage = new MarketModel();
                            if (object.getString("image" + j).length() > 0
                                    && !object.getString("image" + j).equals(
                                    "null")) {
                                coImage.setImage_path(object.getString("image"
                                        + j));
                            }
                            comImages.add(coImage);
                        }
                        bModel.setUser_photo_imgpath(object
                                .getString("member_avatar"));
                        bModel.setUser_name(object.getString("member_name"));
                        bModel.setCommentsImg(comImages);
                        bModel.setPublish_time(TimeDiffer.caculatedDate(object
                                .getLong("create_time")));
                        blogListModels.add(bModel);
                    }
                    if (blogListModels.size() > 0) {
                        bloglist.setVisibility(View.VISIBLE);
                        blog_null.setVisibility(View.GONE);
                        blogadapter = new BlogListAdapter(getActivity(),
                                blogListModels);
                        bloglist.setAdapter(blogadapter);
                        MineFragment.resetViewPagerHeight(0);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        MineFragment.resetViewPagerHeight(0);
    }

    public void getData() {
        BaseClient.get("Logs", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("blog", response.toString());
                aCache.put("blog", response);
                try {
                    JSONObject value = response.getJSONObject("value");
                    JSONArray content = value.getJSONArray("content");
                    blogListModels.clear();
                    for (int i = 0; i < content.length(); i++) {
                        JSONObject object = content.getJSONObject(i);
                        BlogModel bModel = new BlogModel();
                        bModel.setComment(object.getString("content"));
                        bModel.setCommentID(object.getString("id"));
                        List<MarketModel> comImages = new ArrayList<MarketModel>();
                        for (int j = 1; j < 7; j++) {

                            if (object.getString("image" + j).length() > 0
                                    && !object.getString("image" + j).equals(
                                    "null")) {
                                MarketModel coImage = new MarketModel();
                                coImage.setImage_path(object.getString("image"
                                        + j));
                                comImages.add(coImage);
                            }
                        }
                        bModel.setUser_photo_imgpath(object
                                .getString("member_avatar"));
                        bModel.setUser_name(object.getString("member_name"));
                        bModel.setCommentsImg(comImages);
                        bModel.setPublish_time(TimeDiffer.caculatedDate(object
                                .getLong("create_time")));
                        blogListModels.add(bModel);
                    }
                    if (blogListModels.size() > 0) {
                        bloglist.setVisibility(View.VISIBLE);
                        blog_null.setVisibility(View.GONE);
                        blogadapter = new BlogListAdapter(getActivity(),
                                blogListModels);
                        bloglist.setAdapter(blogadapter);
                    }
                    MineFragment.resetViewPagerHeight(0);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

    public class BlogListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        public List<BlogModel> _listData;
        private Context mContext;

        public BlogListAdapter(Context context, List<BlogModel> list) {

            _listData = new ArrayList<BlogModel>();
            _listData = list;
            this.mContext = context;
            inflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return _listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return _listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return Long.valueOf(_listData.get(position).getCommentID());
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item_blog, null);
                holder.image = (NoScrollGridView) convertView.findViewById(R.id.image_grid_view);
                holder.comments_contents = (TextView) convertView.findViewById(R.id.comment_detail);
                holder.imge_container = (HorizontalScrollView) convertView.findViewById(R.id.image_container);
                holder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
                holder.user_name = (TextView) convertView.findViewById(R.id.comment_user_name);
                holder.user_photo = (CircleImageView) convertView.findViewById(R.id.comments_user_photo);
                holder.praise_imge = (ImageView) convertView.findViewById(R.id.comments_praise);
                holder.deleteBlog = (LinearLayout) convertView.findViewById(R.id.delete_blog);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imge_container.setVisibility(View.VISIBLE);
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            // getWindowManager().getDefaultDisplay().getMetrics(dm);
            float density = dm.density;
            int allWidth = (int) (110 * _listData.get(position)
                    .getCommentsImg().size() * density);
            int itemWidth = (int) (100 * density);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    allWidth, LinearLayout.LayoutParams.FILL_PARENT);
            holder.image.setLayoutParams(params);
            holder.image.setColumnWidth(itemWidth);
            holder.image.setHorizontalSpacing(10);
            holder.image.setStretchMode(GridView.NO_STRETCH);

            holder.image.setNumColumns(_listData.get(position).getCommentsImg().size());
            holder.image.setAdapter(new MarketAdapter(mContext, _listData.get(
                    position).getCommentsImg()));
            holder.image.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position1, long id) {
                    // TODO Auto-generated method stub
					Intent intent = new Intent(mContext,ImageActivity.class);
					intent.putExtra("image_path", _listData.get(position).getCommentsImg().get(position1).getImage_path());
					mContext.startActivity(intent);
                }
            });
            holder.comments_contents.setText(_listData.get(position).getComment());

            holder.praise_imge.setImageResource(R.drawable.delete_blog);
            holder.deleteBlog.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Log.e("id", _listData.get(position).getCommentID());
                    deleteBlog(_listData.get(position).getCommentID());
                    _listData.remove(position);
                    if (_listData.size() == 0) {
                        bloglist.setVisibility(View.GONE);
                        blog_null.setVisibility(View.VISIBLE);
                    }
                    notifyDataSetChanged();
                }
            });
            holder.user_name.setText(_listData.get(position).getUser_name());
            ImageLoader.getInstance().displayImage(_listData.get(position).getUser_photo_imgpath(), holder.user_photo);
            holder.publish_time.setText(_listData.get(position).getPublish_time());
            return convertView;
        }

        public class ViewHolder {
            private CircleImageView user_photo;
            private TextView user_name;
            private TextView comments_contents;
            private TextView publish_time;
            private HorizontalScrollView imge_container;
            private ImageView praise_imge;
            private LinearLayout deleteBlog;
            private NoScrollGridView image;
        }

        private void deleteBlog(String id) {
            RequestParams params = new RequestParams();
            params.add("id", id);
            BaseClient.delete("Logs", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      JSONObject response) {
                    // TODO Auto-generated method stub
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getInt("code") == 0) {
                            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}