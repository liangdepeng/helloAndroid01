package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.zhengjun.helloandroid.view.PhotoView;

public class ImageActivity extends Activity{
	private PhotoView image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		image = (PhotoView)findViewById(R.id.image);
		image.enable();
		String imagepath ="http://api.reactorlive.com/media/" +getIntent().getExtras().getString("image_path")+"/w/1000/h/2000/m/0";
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(imagepath, image);
		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

}
