package com.siboren.android.foodofchina;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements MyOrientationListener.OnOrientationListener{

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private List<String> permissionList;

    private Boolean isFirstLocate=true;
    private float mCurrentAccracy;
    private double mCurrentLantitude;
    private double mCurrentLongitude;
    private int mXDirection;
    public LocationClient mlocation;
    private MyLocationData.Builder builder;
    private MyOrientationListener myOrientationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mlocation = new LocationClient(getApplicationContext());
        mlocation.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_map);
        //初始化方向传感器
        myOrientationListener = new MyOrientationListener(this);
        myOrientationListener.setOnOrientationListener(this);
        //获取地图控件
        mMapView = findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        //申请权限
        initPermission();
    }

    private void initPermission() {

        permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MapActivity.this, permissions, 1);
        } else {
            //请求定位
            requestLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MapActivity.this, "必须同意所有权限", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    //请求定位
                    requestLocation();
                } else {
                    finish();
                }
                break;
        }
    }

    private void requestLocation() {
        //设置定位属性
        initLocation();
        //开始定位
        mlocation.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置百度坐标
        option.setCoorType("bd09ll");
        option.setOpenGps(true); // 打开gps
        //设置定位延迟
        option.setScanSpan(3000);
        mlocation.setLocOption(option);
    }

    @Override
    public void onOrientationChanged(float x) {
        mXDirection = (int) x;
        builder = new MyLocationData.Builder();
        builder.accuracy(mCurrentAccracy);
        builder.direction(mXDirection);
        builder.latitude(mCurrentLantitude);
        builder.longitude(mCurrentLongitude);
        MyLocationData locationData = builder.build();
        mBaiduMap.setMyLocationData(locationData);
        // 设置自定义图标
        //BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
        mBaiduMap.setMyLocationConfiguration(configuration);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mCurrentAccracy=bdLocation.getRadius();
            mCurrentLantitude=bdLocation.getLatitude();
            mCurrentLongitude=bdLocation.getLongitude();
            if (bdLocation.getLocType() == BDLocation.TypeGpsLocation || bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                //设置地图显示
                navigateTo(bdLocation);
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }

        private void navigateTo(BDLocation location) {
            if (isFirstLocate) {
                //定位坐标
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                //设置缩放级别
                update = MapStatusUpdateFactory.zoomTo(21f);
                mBaiduMap.animateMapStatus(update);
                isFirstLocate = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mlocation.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
        myOrientationListener.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myOrientationListener.stop();
    }

}