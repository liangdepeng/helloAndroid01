package com.example.zhengjun.helloandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

/**
 * Created by Tony on 2018/5/8.
 */

public class MerchantLocationActivity extends Activity {
    private ImageView mer_back;
    private TextView mer_title;
    private String latatitude = "";
    private String longtitude = "";
    private String title;
    private BaiduMap mBaiduMap = null;
    private MapView mMapView = null;
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "gcj02";
    private RoutePlanSearch routSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_merchant);
        init();
    }
    private void init(){
        title = getIntent().getExtras().getString("mer_name");
        longtitude = getIntent().getExtras().getString("mer_long");
        latatitude = getIntent().getExtras().getString("mer_lat");
        mer_back = (ImageView) findViewById(R.id.merchant_back);
        mer_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                finish();
            }
        });
        mer_title = (TextView) findViewById(R.id.mer_title);
        mer_title.setText(title);
        mMapView = (MapView) findViewById(R.id.mer_map);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        LatLng point = new LatLng(30.326912, 120.35043);
// 构建 Marker 图标
        BitmapDescriptor bitmap =
                BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
// 构建 MarkerOption ，用于在地图上添加 Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
// 在地图上添加 Marker ，并显示
        mBaiduMap.addOverlay(option);

        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());
        initLocation();
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
// TODO Auto-generated method stub
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(location.getRadius())
                        .direction(100) //  此处设置开发者获取到的方 向信息，顺时针 0-360
                        .latitude(location.getLatitude())
                        .longitude(location.getLongitude())
                        .build();
                mBaiduMap.setMyLocationData(locData);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                routSearch = RoutePlanSearch.newInstance();
                LatLng startCenter = new LatLng(location.getLatitude(), location.getLongitude());
                LatLng endCenter = new LatLng((Float.valueOf(latatitude) / 10000000),
                        (Float.valueOf(longtitude) / 10000000));
                routSearch.setOnGetRoutePlanResultListener(routListener);
                PlanNode endNode = PlanNode.withLocation(endCenter);
                PlanNode startNode = PlanNode.withLocation(startCenter);
                routSearch.walkingSearch((new
                        WalkingRoutePlanOption()).from(startNode).to(endNode));
            }
        });
        mLocationClient.start();
    }

    OnGetRoutePlanResultListener routListener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult result) {
// TODO Auto-generated method stub
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                Toast.makeText(MerchantLocationActivity.this, " 抱歉 ， 未找到结果",
                        Toast.LENGTH_SHORT).show();
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
//result.getSuggestAddrInfo()
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        }
        @Override
        public void onGetTransitRouteResult(TransitRouteResult arg0) {
// TODO Auto-generated method stub
        }
        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult arg0) {
// TODO Auto-generated method stub
        }
    };
    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        mLocationClient.stop();
        routSearch.destroy();
        mMapView.onDestroy();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);
// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);// 可选，默认 gcj02 ，设置返回的定位结果坐标系
        option.setScanSpan(0);
// 可选，默认 0 ，即仅定位一次，设置发起定位请求的间隔需要大于等于 1000ms 才是有效的
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);// 可选，默认 false, 设置是否使用 gps
        option.setLocationNotify(true);
// 可选，默认 false ，设置是否当 gps 有效时按照 1S1 次频率输出 GPS 结果
        option.setIgnoreKillProcess(true);
// 可选，默认 true ，定位 SDK 内部是一个 SERVICE ，并放到了独立进程，
// 设置是否在 stop 的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);
// 可选，默认 false ，设置是否需要过滤 gps 仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);
// 可选，默认 false ，设置是否需要位置语义化结果，
// 可以在 BDLocation.getLocationDescribe 里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);// 可选，默认 false ，设置是否需要 POI 结果，
// 可以在 BDLocation.getPoiList 里得到
        mLocationClient.setLocOption(option);
    }
}
