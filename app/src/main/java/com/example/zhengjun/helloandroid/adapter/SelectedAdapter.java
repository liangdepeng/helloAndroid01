package com.example.zhengjun.helloandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.model.Selected;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import uk.co.deanwild.flowtextview.FlowTextView;

public class SelectedAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	public List<Selected> _listData = null;
	private Context mContext;
	public  SelectedAdapter(Context context,List<Selected> list){
		
		_listData = new ArrayList<Selected>();
		_listData = list;
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listData == null ? 0 : _listData.size();
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
		final View view = convertView;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.selected_listview_item, null);
			holder.selected_big_img = (ImageView)convertView.findViewById(R.id.selected_big_img);
			holder.selected_big_img_text=(TextView)convertView.findViewById(R.id.selectd_big_img_text);
			holder.brief_text=(FlowTextView)convertView.findViewById(R.id.brief_text);
			holder.selected_activity_date=(TextView)convertView.findViewById(R.id.selected_activity_date);
			holder.collect_action=(ImageView)convertView.findViewById(R.id.collect_action);
			holder.selected_collection_info=(TextView)convertView.findViewById(R.id.selected_collection_info);
			holder.select_collect =(RelativeLayout)convertView.findViewById(R.id.select_collect);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		if(_listData.get(position).getSelected_big_img().length()>0&&!_listData.get(position).getSelected_big_img().equals("null")){
			WindowManager windowManager =(WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
			int imagesWidth = windowManager.getDefaultDisplay().getWidth();
			String url ="http://api.reactorlive.com/media/" +
					_listData.get(position).getSelected_big_img()+"/w/"+imagesWidth+"/h/"+imagesWidth*0.6+" /m/0";
			ImageLoader.getInstance().displayImage(url, holder.selected_big_img);
		}


		holder.selected_big_img_text.setText(_listData.get(position).getSelected_big_img_text());
		holder.brief_text.setTextSize(mContext.getResources().getDimension(R.dimen.select_flow_textSize));
		holder.brief_text.setTextColor(mContext.getResources().getColor(R.color.sl_artical_detail));
		holder.brief_text.setText(_listData.get(position).getBrief_text());
		holder.selected_activity_date.setText(_listData.get(position).getPublish_time());
		if(_listData.get(position).getIs_collect().equals("1")){
			holder.collect_action.setImageResource(R.drawable.collecting_active);
		}else{
			holder.collect_action.setImageResource(R.drawable.collecting_normal);
		}
		holder.selected_collection_info.setText(String.valueOf(_listData.get(position).getSelected_collection_info()));
		
		return convertView;
	}
	
	public class ViewHolder{
		private ImageView selected_big_img;
		private TextView selected_big_img_text;
		//private LinearLayout brief_text;
		private FlowTextView brief_text;
		private TextView selected_activity_date;
		private ImageView collect_action;
		private TextView selected_collection_info;
		private RelativeLayout select_collect;
  	}

	
}
