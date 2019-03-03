
package com.qidu.jiajie.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.qidu.jiajie.AppApplication;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAMapLocationUtil {
    //定位相关
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private volatile static MyAMapLocationUtil singleton;
    private LocationCallBack locationCallBack;
    private MyAMapLocationUtil(){}
    public static MyAMapLocationUtil getSingleton() {
        if (singleton == null) {
            synchronized (MyAMapLocationUtil.class) {
                if (singleton == null) {
                    singleton = new MyAMapLocationUtil();
                }
            }
        }
        return singleton;
    }


    public void startLocationCurrentPosition(LocationCallBack locationCallBack) {
        destroyLocation();
        if(locationClient==null){
            //初始化client
            locationClient = new AMapLocationClient(AppApplication.getInstance());
            locationOption = getDefaultOption();
            //设置定位参数
            locationClient.setLocationOption(locationOption);
            this.locationCallBack=locationCallBack;
            // 设置定位监听
            locationClient.setLocationListener(new MyAMapLocationListener());
            //启动后台定位，第一个参数为通知栏ID，建议整个APP使用一个
            //locationClient.enableBackgroundLocation(2001, buildNotification());


        }
        // 启动定位
        locationClient.startLocation();
    }

    public interface LocationCallBack{
        void locationSuccess(AMapLocation location);
        void locationFailure();
    }

    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        //mOption.setInterval(5000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Transport);//设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
        return mOption;
    }

    public void destroyLocation() {
        if (null != locationClient) {
            locationClient.stopLocation();
            //关闭后台定位，参数为true时会移除通知栏，为false时不会移除通知栏，但是可以手动移除
            //locationClient.disableBackgroundLocation(true);
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


    /**
     * 定位监听
     */

    private class MyAMapLocationListener implements AMapLocationListener {

        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    destroyLocation();
                    if(locationCallBack!=null){
                        locationCallBack.locationSuccess(location);
                    }
                } else {
                    //ToastUtils.show(location.getErrorInfo());
                    Log.d("aaaaaaa",location.getErrorInfo());
                    if(locationCallBack!=null){
                        locationCallBack.locationFailure();
                    }
                }
            } else {
                if(locationCallBack!=null){
                    locationCallBack.locationFailure();
                }
            }
        }
    };


}

