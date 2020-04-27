package com.example.zhuffei.ffei.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.example.zhuffei.ffei.FfeiApplication;
import com.example.zhuffei.ffei.R;
import com.example.zhuffei.ffei.tool.WsManager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends Activity implements LocationSource, AMapLocationListener {
    private String targetAccid;
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;// 高德相关
    private boolean isFirst = true;
    private static double lat, lng;//实时定位的经纬度
    private double latitude, longitude;//接收共享位置的经纬度
    private Marker marker, markerOwner;//接收的marker,自己位置的marker
    private WsManager wsManager;
    private TextView tip;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    tip.setText("正在共享位置");
                    break;
                case 1:
                    tip.setText("对方尚未加入");
                    break;
            }
       }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        String accid1 = getIntent().getStringExtra("accid1");
        String accid2 = getIntent().getStringExtra("accid2");
        targetAccid = FfeiApplication.user.getAccid().equals(accid1) ? accid2 : accid1;
        mapView = findViewById(R.id.map);
        tip = findViewById(R.id.tip);
        tip.getBackground().setAlpha(180);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initMap();
        wsManager = WsManager.getInstance();
        wsManager.setMapActivity(this);
        wsManager.init();
    }


    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        setUpMap();
    }

    /**
     * 设置地图属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationType(AMap.MAP_TYPE_SATELLITE);// 跟随模式
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
//                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
                if (isFirst) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()), 18));//定位成功移到当前定位点
                    isFirst = false;
                }
                lat = aMapLocation.getLatitude();
                lng = aMapLocation.getLongitude();
                Map map = new HashMap<String, Object>();
                map.put("type", "normal");
                map.put("target", targetAccid);
                map.put("lat", lat);
                map.put("lng", lng);
                String json = JSON.toJSONString(map);
                wsManager.sendMessage(json);
                if (markerOwner != null) {
                    markerOwner.remove();//每次定位发生改变的时候,把自己的marker先移除再添加
                }
                    markerOwner = aMap.addMarker((help_add_icon(new LatLng(lat, lng), R.mipmap.icon_myp)));
            } else {
                Log.i("aaaaaaaa", aMapLocation.getErrorCode() + "错误码" + aMapLocation.getErrorInfo() + "错误信息");
            }
        }
    }

    //激活定位
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(MapActivity.this);
            mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);// 设置定位监听
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mlocationClient.setLocationOption(mLocationOption);// 设置为高精度定位模式
            mLocationOption.setInterval(1000);
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        WsManager.getInstance().disconnect();
    }

    public static double getLat() {
        return lat;
    }

    public static double getLng() {
        return lng;
    }

    /**
     * 添加所接收到的共享位置信息
     *
     * @param
     */
    public void allLatLng(String text) {
        try {
//            if (list.size() != 0) {
//                Remove(list);
//            }
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());
//                latitude = jsonObject.getDouble("lat");
//                longitude = jsonObject.getDouble("lng");
//                LatLng latLng = new LatLng(latitude, longitude);
//                marker = aMap.addMarker(help_add_icon(latLng, R.mipmap.icon_tourist));
//                list.add(marker);
//            }

            Map map = JSON.parseObject(text, HashMap.class);
            String type = (String) map.get("type");
            String accid = (String) map.get("accid");
            if (type.equals("close") && accid.equals(targetAccid)) {
                handler.sendEmptyMessage(1);
                marker.remove();
                marker = null;
                return;
            }
            if (marker == null && type.equals("normal")) {
                handler.sendEmptyMessage(0);
            }
            BigDecimal lat = (BigDecimal) map.get("lat");
            BigDecimal lng = (BigDecimal) map.get("lng");
            LatLng latLng = new LatLng(lat.doubleValue(), lng.doubleValue());
            if (null != marker) {
                marker.remove();
            }
            marker = aMap.addMarker(help_add_icon(latLng, R.mipmap.icon_tourist));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aaaaaaa", "解析出错" + e.getMessage());
        }
    }

    /**
     * 手机上显示共享位置的图标
     *
     * @param latLng
     * @param id
     * @return
     */
    public static MarkerOptions help_add_icon(LatLng latLng, int id) {
        MarkerOptions markOptiopns = new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(id));
        return markOptiopns;
    }
}


