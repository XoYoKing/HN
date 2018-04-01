//package com.example.admin.hn.ui.account;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.ContextCompat;
//
//import com.example.admin.hn.R;
//import com.example.admin.hn.base.BaseActivity;
//import com.example.admin.hn.utils.MarkerOverlay;
//import com.example.admin.hn.utils.ToolAlert;
//
//import java.util.ArrayList;
//import java.util.List;
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class MapActivity extends BaseActivity {
//
//    @Bind(R.id.map)
//    public MapView mMapView;
//
//    private MarkerOverlay markerOverlay;
//    private AMap aMap;
//    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
//    private Bundle savedInstanceState;
//    private LatLng center = new LatLng(39.993167, 116.473274);// 中心点
//    ArrayList<String> x =new ArrayList<>();
//    ArrayList<String> y=new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//        ButterKnife.bind(this);
//        initTitleBar();
//        initData();
//    }
//
//    @Override
//    public void initTitleBar() {
//        super.initTitleBar();
//        Intent intent =getIntent();
//        x = (ArrayList<String>)intent.getSerializableExtra("longitude");
//        y = (ArrayList<String>)intent.getSerializableExtra("latitude");
//    }
//
//    @Override
//    public void initData() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            //这里以ACCESS_COARSE_LOCATION为例
//            if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                    != PackageManager.PERMISSION_GRANTED) {
//                //申请WRITE_EXTERNAL_STORAGE权限
//                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//                        WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
//            } else {
//                //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
//                mMapView.onCreate(savedInstanceState);
//                if (aMap == null) {
//                    aMap = mMapView.getMap();
//                }
//            }
//        }else {
//            mMapView.onCreate(savedInstanceState);
//            if (aMap == null) {
//                aMap = mMapView.getMap();
//            }
//        }
////        LatLng latLng = new LatLng(39.906901,116.397972);
////        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title("北京").snippet("DefaultMarker"));
//        aMap.setOnMapLoadedListener(this); //地图加载完成监听
//
//    }
//
//
//    @Override
//    public void onDestroy() {
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        mMapView.onDestroy();
//        super.onDestroy();
//        markerOverlay.removeFromMap();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
//        mMapView.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
//        mMapView.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
//        mMapView.onSaveInstanceState(outState);
//    }
//
//
//    //权限回调
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        doNext(requestCode, grantResults);
//    }
//
//
//
//    private void doNext(int requestCode, int[] grantResults) {
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission Granted
//                mMapView.onCreate(savedInstanceState);
//                if (aMap == null) {
//                    aMap = mMapView.getMap();
//                }
//            } else {
//                // Permission Denied
//                ToolAlert.showToast(MapActivity.this, "请打开地图所需权限！", false);
//            }
//        }
//    }
//
//    private List<LatLng> getPointList() {
//        List<LatLng> pointList = new ArrayList<LatLng>();
//        for (int i=0;i<x.size();i++){
//            pointList.add(new LatLng( Double.valueOf(x.get(i)), Double.valueOf(y.get(i))));
//        }
//        center = new LatLng(Double.valueOf(x.get(0)), Double.valueOf(y.get(0)));
//        return pointList;
//    }
//
//
//    @Override
//    public void onMapLoaded() {
//        //添加MarkerOnerlay
//        markerOverlay = new MarkerOverlay(aMap, getPointList(),getPointList().get(0));
//        markerOverlay.addToMap();
//        markerOverlay.zoomToSpanWithCenter();
//        aMap.addPolyline(new PolylineOptions().
//                addAll(getPointList()).width(10).color(Color.argb(255,255,248,161)).setDottedLine(true));
//    }
//
//}
