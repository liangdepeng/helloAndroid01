package com.shizhefei.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhengjun.helloandroid.ChangeMineInfoActivity;
import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.popwindow.MineLoginPopWindow;
import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.utils.Bimp;
import com.example.zhengjun.helloandroid.utils.FileManagerUtils;
import com.example.zhengjun.helloandroid.utils.Helper;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.example.zhengjun.helloandroid.view.CircleImageView;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class MineFragment extends LazyFragment{

	public static ViewPager viewPager;
	private ACache mACache;
	private TextView title;
	private ImageView left_button;
	private ImageView right_img;
	private CircleImageView user_photo;
	private TextView user_name;
	private TextView user_sign;
	private TextView concern_number;
	private TextView fas_number;
	private TextView album_number;

	private String tempPicPath = "";
	private String sex = "";
	private String nickname = "";
	private String sign = "";

	public void initViewPager(){
		viewPager= (ViewPager) findViewById(R.id.fragment_mine_viewPager);
		Indicator indicator = (Indicator) findViewById(R.id.fragment_mine_indicator);
		indicator.setScrollBar(new ColorBar(getApplicationContext(),
				getResources().getColor(R.color.pink), 5));
		Resources res = getResources();
		int selectColor = res.getColor(R.color.pink);
		int unSelectColor = res.getColor(R.color.tab_top_text_1);
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColor(selectColor, unSelectColor).setSize(17, 17));
		viewPager.setOffscreenPageLimit(4);
		IndicatorViewPager indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
//  注意这里 的 FragmentManager  是 getChildFragmentManager();  因为是在 Fragment 里面
//  而在 activity 里面用 FragmentManager  是 getSupportFragmentManager()
		indicatorViewPager.setAdapter(new MyAdapter(getChildFragmentManager()));
	}
	private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}
		@Override
		public int getCount() {
			return 3;
		}
		@Override
		public View getViewForTab(int position, View convertView,
								  ViewGroup container) {
			LayoutInflater inflate = LayoutInflater.from(getApplicationContext());
			switch (position) {
				case 0:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView blog = (TextView) convertView;
					blog.setText(" 日志");
					break;
				case 1:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView comments = (TextView) convertView;
					comments.setText(" 评论");
					break;
				case 2:
					if (convertView == null) {
						convertView = inflate.inflate(R.layout.tab_top, container, false);
					}
					TextView collections = (TextView) convertView;
					collections.setText(" 收藏");
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
					BlogFragment blogFragment = new BlogFragment();
					fragment = blogFragment;
					break;
				case 1:
					CommentFragment commentsFragment = new CommentFragment();
					fragment = commentsFragment;
					break;
				case 2:
					CollectionFragment collectionFragment = new CollectionFragment();
					fragment = collectionFragment;
					break;
				default:
					break;
			}
			return fragment;
		}
	}

	public static void resetViewPagerHeight(int position) {
		if(viewPager == null) {
			return;
		}
		View child = viewPager.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams)
					viewPager.getLayoutParams();
			params.height = h;
			viewPager.setLayoutParams(params);
		}
	}






	public void change_photo(){
		final PopupWindow pop = new PopupWindow(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		Button bt1 = (Button) view
				.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view
				.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view
				.findViewById(R.id.item_popupwindows_cancel);
		bt3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
// TODO Auto-generated method stub
				pop.dismiss();
			}
		});
		bt1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
// TODO Auto-generated method stub
				String strImgPath = FileManagerUtils.getAppPath("reactor") + "/";
				String fileName = System.currentTimeMillis() + ".jpg";//  照片命名
				File out = new File(strImgPath);
				if (!out.exists()) {
					out.mkdirs();
				}
				out = new File(strImgPath, fileName);
				Uri uri = Uri.fromFile(out);
				tempPicPath = uri.getPath();
				Log.e("YanZi", "takePicture, uri.toString() = " + uri.toString() + "uri.getPath()" + uri.getPath());
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
				startActivityForResult(intent, 0);
				pop.dismiss();
			}
		});
		bt2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 1);
				pop.dismiss();
			}
		});
		pop.showAtLocation(findViewById(R.id.mine_fragment), Gravity.CENTER, 0, 0);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.e("requestcode", "" + requestCode);
		Log.e("resultCode", "" + resultCode);
		if (requestCode == 0 && resultCode == -1) {
//  拍照返回
			Bitmap bitmap = null;
			try {
				bitmap = Helper.getInstance().rotaingImageView(Helper.getInstance().readPictureDegree(tempPicPath), Bimp.revitionImageSize(tempPicPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			tempPicPath = Helper.getInstance().saveMyBitmap(bitmap);
			uploadImage(new File(tempPicPath));
		} else if (requestCode == 1 && resultCode == -1) {
//  选择图片返回
			tempPicPath = getPath(data.getData());
			Bitmap bitmap = null;
			try {
				bitmap =
						Helper.getInstance().rotaingImageView(Helper.getInstance().readPictureDegree(tempPicPath), Bimp.revitionImageSize(tempPicPath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			tempPicPath = Helper.getInstance().saveMyBitmap(bitmap);
			uploadImage(new File(tempPicPath));
		}
	}
	private void uploadInfo(String imagpath) {
		RequestParams params = new RequestParams();
		params.put("avatar", "http://api.reactorlive.com/media/" + imagpath +
				"/w/200/h/200/m/0");
		params.put("edit", "1");
		BaseClient.put("member", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  JSONObject response) {
// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
			}
		});
	}



	private void uploadImage(File img) {
		RequestParams params = new RequestParams();
		try {
			params.put("image[]", img);
		} catch (FileNotFoundException e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BaseClient.postFile("media/image", params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, Header[] headers,
										  JSONObject response) {
// TODO Auto-generated method stub
						super.onSuccess(statusCode, headers, response);
						try {
							Log.e("upload Image success", response.toString());
							JSONObject value = response.getJSONObject("value");
							uploadInfo(value.getString("image.0"));
							ImageLoader.getInstance().displayImage("file://" + tempPicPath,
									user_photo);
						} catch (JSONException e) {
// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, Throwable error,
										  JSONObject jsonObject) {
						Log.e("error", jsonObject.toString());
					}
					@Override
					public void onFailure(int statusCode, Header[] headers, String str,
										  Throwable error) {
						Log.e("error", str.toString());
					}
				});
	}

	public String getPath(Uri uri){
		if(uri == null) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null,
				null);
		if(cursor != null) {
//5.0 及以下
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			for (int i = 0; i < cursor.getColumnCount(); i++) {//  取得图片 uri 的列名和此列的详细信息
				Log.i("YanZi", i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
			}
			return cursor.getString(column_index);
		} else {
//5.0 以上
			return uri.getPath();
		}
	}




	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);

		setContentView(R.layout.fragment_mine);
		init();
		initViewPager();
	}

	@Override
	protected void onResumeLazy() {
// TODO Auto-generated method stub
		super.onResumeLazy();
		getData();
		Log.e("MineFragment", "on resume");
	}
	private void clearData(){
		user_photo.setImageDrawable(null);
		user_name.setText("");
		user_sign.setText("");
		concern_number.setText("");
		fas_number.setText("");
		album_number.setText("");
	}

	private void logout() {
		Log.e("MineFragment", "logout");
		BaseClient.post("member/logout", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  JSONObject response) {
// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				Toast.makeText(getActivity(), "退出当前帐号成功",
						Toast.LENGTH_SHORT).show();
				clearData();
				getData();
			}
		});
	}

	public void init() {
		mACache = ACache.get(getActivity());
		title = (TextView) findViewById(R.id.title);
		title.setText("个人中心");
		title.setTextColor(getResources().getColor(R.color.mine_title_color));
		left_button = (ImageView) findViewById(R.id.canlendar);
		left_button.setVisibility(View.INVISIBLE);
		right_img = (ImageView) findViewById(R.id.setting);
		right_img.setImageResource(R.drawable.edit);
		right_img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(getActivity(), ChangeMineInfoActivity.class);
				intent2.putExtra("sex", sex);
				intent2.putExtra("nickname", nickname);
				intent2.putExtra("sign", sign);
				getActivity().startActivity(intent2);
			}
		});

		user_photo = (CircleImageView) findViewById(R.id.user_photo_mine);
		user_photo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				change_photo();
			}
		});


		user_name = (TextView) findViewById(R.id.user_name);
		user_sign = (TextView) findViewById(R.id.user_sign);
		concern_number = (TextView) findViewById(R.id.concern_number);
		fas_number = (TextView) findViewById(R.id.fans_number);
		album_number = (TextView) findViewById(R.id.album_number);
		if (!PublicUtils.isNetworkAvailable(getActivity())) {
			if (mACache.getAsJSONObject("member_info") != null) {
				JSONObject response = mACache.getAsJSONObject("member_info");
				try {
					JSONObject value = response.getJSONObject("value");
					String url = value.getString("avatar");
					ImageLoader.getInstance().displayImage(url, user_photo);



					//user_name.setText(value.getString("nickname"));
					//user_sign.setText(value.getString("sign"));
					sex = value.getString("sex");
					nickname = value.getString("nickname");
					sign = value.getString("sign");
					user_name.setText(nickname);
					user_sign.setText(sign);

					concern_number.setText(value.getString("attention_number"));
					fas_number.setText(value.getString("fans_number"));
					album_number.setText(value.getString("photo_number"));
				} catch (JSONException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void getData() {
		BaseClient.get("member", null, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
								  JSONObject response) {
// TODO Auto-generated method stub
				Log.e("Success", response.toString());
				mACache.put("member_info", response);
				try {
					JSONObject value = response.getJSONObject("value");
					String url = value.getString("avatar");
					ImageLoader.getInstance().displayImage(url, user_photo);
					sex = value.getString("sex");
					nickname = value.getString("nickname");
					sign = value.getString("sign");
					try {
						sign = URLDecoder.decode(sign, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					user_name.setText(nickname);
					user_sign.setText(sign);



					//user_name.setText(value.getString("nickname"));
					//user_sign.setText(value.getString("sign"));
					concern_number.setText(value.getString("attention_number"));
					fas_number.setText(value.getString("fans_number"));
					album_number.setText(value.getString("photo_number"));
				} catch (JSONException e) {
// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers,
								  Throwable throwable, JSONObject errorResponse) {
// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
				View v = getActivity().getWindow().getDecorView();
				v.setDrawingCacheEnabled(true);
				v.buildDrawingCache(true);
				Bitmap bmp1 = v.getDrawingCache();
				MineLoginPopWindow login = new MineLoginPopWindow(getActivity(), bmp1);
				login.showAtLocation(
						getActivity().findViewById(R.id.mine_fragment),
						Gravity.CENTER | Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
						0, 0);
			}
		});
	}

}
