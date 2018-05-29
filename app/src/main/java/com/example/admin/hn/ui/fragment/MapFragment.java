package com.example.admin.hn.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.hn.MainActivity;
import com.example.admin.hn.R;
import com.example.admin.hn.base.BaseFragment;
import com.example.admin.hn.base.HNApplication;
import com.example.admin.hn.utils.ToolAlert;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author duantao
 * @date on 2017/7/26 16:04
 * @describe 地图
 */
public class MapFragment extends BaseFragment {

    @Bind(R.id.tv_top_title)
    TextView tv_top_title;
    @Bind(R.id.iv_two)
    ImageView iv_two;
    @Bind(R.id.iv_one)
    ImageView iv_one;
    @Bind(R.id.iv_search)
    ImageView iv_search;
    @Bind(R.id.web)
    WebView mWebView;
    @Bind(R.id.tv_text)
    EditText tv_text;
    @Bind(R.id.sp)
    Spinner sp;

    private String url = null;
    View view;
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 1;
    private Bundle savedInstanceState;
    private List<String> sp_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        this.savedInstanceState = savedInstanceState;
        init();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init() {
        tv_top_title.setText("船位");
        iv_two.setVisibility(View.GONE);
        iv_one.setVisibility(View.GONE);
        iv_search.setVisibility(View.GONE);
        sendHttp();
    }

    private void sendHttp(){
        setSpData();
    }

    /**
     * 初始化 审核状态选择器
     */
    private void setSpData() {
        //数据
        for (int i = 0; i < MainActivity.list.size(); i++) {
            sp_list.add(MainActivity.list.get(i).shipname);
        }
        //适配器
        ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, sp_list);
        //加载适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String shipname = sp_list.get(position);
               Logger.e("name", shipname + "");
               ship(shipname);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ship(MainActivity.list.get(0).shipname);
    }

    private void setUrl(String shipName){
        url = "http://api.shipxy.com/apicall/location?k=a7222a0180264aa99245ff2b53595a31&kw=" +shipName+ "&tip=1&track=1";
    }

    @OnClick({R.id.bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                if (tv_text.getText().toString().length() < 1) {
                    ToolAlert.showToast(getActivity(), "船舶名称不能为空", false);
                    return;
                }
                ship(tv_text.getText().toString() );
                break;
        }
    }

    /**
     * 加载船舶信息
     * @param shipName
     */
    private void ship(String shipName){
        if (isFirstHttp) {
            isFirstHttp = false;
            WebSettings ws = mWebView.getSettings();
            //支持javascript
            mWebView.getSettings().setJavaScriptEnabled(true);
            // 设置可以支持缩放
            ws.setSupportZoom(true);
            ws.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        ws.setBuiltInZoomControls(true);//设置出现缩放工具
            //自适应屏幕
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.setWebViewClient(
                    new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }

                    }
            );
        }
        setUrl(shipName);
        if (url != null) {
            mWebView.loadUrl(url);
        }
    }
    //定位
//    private void Location() {
//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;
//
//
////        List<LatLng> latLngs = new ArrayList<LatLng>();
////        latLngs.add(new LatLng(39.999391,116.135972));
////        latLngs.add(new LatLng(39.898323, 116.057694));
////        latLngs.add(new LatLng(39.900430, 116.265061));
////        latLngs.add(new LatLng(39.955192, 116.140092));
////        aMap.addPolyline(new PolylineOptions().
////                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));
//    }
//
//    //权限申请
//    private void init() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            //这里以ACCESS_COARSE_LOCATION为例
//            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
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
//    }
//
//
//    @Override
//    public void onDestroy() {
//        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
//        mMapView.onDestroy();
//        super.onDestroy();
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
//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (!hidden) {
//            init();
//        }
//    }
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
//                ToolAlert.showToast(getActivity(), "请打开地图所需权限！", false);
//            }
//        }
//    }
//

}
