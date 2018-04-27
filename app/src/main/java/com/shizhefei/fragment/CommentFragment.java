package com.shizhefei.fragment;

import android.os.Bundle;

import com.example.zhengjun.helloandroid.R;
import com.shizhefei.fragment.LazyFragment;

public class CommentFragment extends LazyFragment {

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_comments);
	}

}
