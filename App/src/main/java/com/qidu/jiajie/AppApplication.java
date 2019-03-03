package com.qidu.jiajie;

import com.common.lib.base.BaseApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 7du-28 on 2018/1/31.
 */

public class AppApplication extends BaseApplication{

    private static AppApplication context;
    public static boolean isFirst=true;
    public static AppApplication getInstance (){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        //极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

    }
}
