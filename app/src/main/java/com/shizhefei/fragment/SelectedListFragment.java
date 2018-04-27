package com.shizhefei.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.zhengjun.helloandroid.R;
import com.example.zhengjun.helloandroid.SelectedDetailActivity;
import com.example.zhengjun.helloandroid.adapter.SelectedAdapter;
import com.example.zhengjun.helloandroid.model.Selected;
import com.example.zhengjun.helloandroid.utils.ACache;
import com.example.zhengjun.helloandroid.utils.BaseClient;
import com.example.zhengjun.helloandroid.utils.PublicUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.markmao.pulltorefresh.widget.XListView;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SelectedListFragment extends LazyFragment implements XListView.IXListViewListener{

    // private ListView select_list;
    private XListView select_list;

    private ACache aCache;

    private List<Selected> _list_date = new ArrayList<Selected>();

    private int total=1;
    private int page=0;
    private int size=20;
    public int new_page=0;

    public static int prise_number;
    private Handler mHandler;
    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_selected_list);

        try {
            getData();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        init();
    }

    @Override
    public void onRefresh() {
// TODO Auto-generated method stub
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                _list_date = new ArrayList<Selected>();
                new_page = 0;
                try {
                    getData();
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 1);
    }
    @Override
    public void onLoadMore() {
// TODO Auto-generated method stub
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    new_page++;
                    getData();
                } catch (JSONException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, 1);
    }
    //完成数据加载
    private void onLoaded() {
        select_list.stopRefresh(); //停止刷新
        select_list.stopLoadMore(); //停止加载更多
        select_list.setRefreshTime(getTime());
    }
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    public void init() {
        mHandler = new Handler();
        //select_list = (ListView) findViewById(R.id.selected_list);
        select_list = (XListView) findViewById(R.id.selected_list);
        select_list.setPullRefreshEnable(true); //允许下拉刷新
        select_list.setPullLoadEnable(true); //允许上拉加载更多
        select_list.setAutoLoadEnable(true); //允许下拉到底部后自动加载
        select_list.setXListViewListener(this);
        select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), SelectedDetailActivity.class);
                intent.putExtra("selected_id", _list_date.get(position - 1).getId());
                startActivity(intent);
            }
        });
        aCache = ACache.get(getActivity());
        if (!PublicUtils.isNetworkAvailable(getActivity())) {
            JSONObject response = aCache.getAsJSONObject("Slected_list");
            // If the response is JSONObject instead of expected JSONArray
            try {
                JSONObject value = response.getJSONObject("value");
                JSONArray jsonArray = value.getJSONArray("content");
                Log.e("content", jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject = (JSONObject) jsonArray.get(i);
                    Selected selected = new Selected();
                    selected.setBrief_text(jsonObject.getString("intro"));
                    selected.setSelected_big_img_text(jsonObject
                            .getString("title"));
                    selected.setSelected_collection_info(jsonObject
                            .getInt("collection_number"));
                    if(prise_number!=0){
                        selected.setSelected_collection_info(prise_number);
                    }
                    selected.setId(jsonObject.getString("id"));
                    selected.setIs_collect(jsonObject.getString("is_collect"));
                    selected.setSelected_big_img(jsonObject.getString("image"));
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "yyyy.MM.dd HH:mm");
                    String time = formatter.format(new Date(jsonObject
                            .getLong("create_time") * 1000));
                    selected.setPublish_time(time);
                    _list_date.add(selected);
                }
                SelectedAdapter adapter = new SelectedAdapter(
                        getApplicationContext(), _list_date);
                select_list.setAdapter(adapter);

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void getData() throws JSONException {
        Log.e("getData", "getData");
        BaseClient.get("Choice", null, new JsonHttpResponseHandler() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                // If the response is JSONObject instead of expected
                // JSONArray
                Log.e("onSuccess", response.toString());
                aCache.put("Slected_list", response);
                try {
                    if (response.getInt("code") == 0) {
                        JSONObject value = response.getJSONObject("value");
                        total = value.getInt("total");
                        size = value.getInt("size");
                        page = (int) Math.ceil(total / size);
                        Log.e("new_page", new_page + "");
                        JSONArray jsonArray = value.getJSONArray("content");
                        Log.e("content", jsonArray.toString());
                        if (new_page <= page) {
                            Log.e("new_page <= page", "true");
                            for (int i = new_page * size; i < (new_page + 1) * size && i < total; i++) {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject = (JSONObject) jsonArray.get(i);
                                Selected selected = new Selected();
                                selected.setBrief_text(jsonObject.getString("intro"));
                                Log.e("sadasd", jsonObject.getString("intro") + "");
                                selected.setSelected_big_img_text(jsonObject.getString("title"));
                                selected.setSelected_collection_info(jsonObject.getInt("collection_number"));
                                selected.setId(jsonObject.getString("id"));
                                selected.setIs_collect(jsonObject.getString("is_collect"));
                                selected.setSelected_big_img(jsonObject.getString("image"));
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                                String time = formatter.format(new Date(jsonObject.getLong("create_time") * 1000));
                                selected.setPublish_time(time);
                                _list_date.add(selected);
                                SelectedAdapter adapter = new SelectedAdapter(getApplicationContext(), _list_date);
                                int index = select_list.getFirstVisiblePosition();
                                View v = select_list.getChildAt(index);
                                int top = (v == null) ? 0 : v.getBottom();
                                select_list.setAdapter(adapter);
                                select_list.setSelectionFromTop(index, top);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                onLoaded();

            }
        });
    }

}

