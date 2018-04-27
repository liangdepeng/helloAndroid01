package com.example.zhengjun.helloandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.model.MarketModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;



public class MarketAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public List<MarketModel> _listData = null;
	private Context mContext;
	public  MarketAdapter(Context context,List<MarketModel> list){
		_listData = new ArrayList<MarketModel>();
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
		return _listData == null ? null : _listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return _listData == null ? 0 : position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_market, null);
			holder.album_img=(ImageView)convertView.findViewById(R.id.my_market_item);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage("http://api.reactorlive.com/media/"+_listData.get(position).getImage_path()+"/w/560/h/260/m/0", holder.album_img);
		return convertView;
	}
	public class ViewHolder{
		private ImageView album_img;
  	}
}
