package com.app.base.utils;


import com.common.lib.retrofit.ApiHttpUrl;

public class HttpUrl {
    public static String urlKey="loadUrl";

    public static String homeUrl="http://vdao_mobile.7dugo.com/";//首页地址

    //前端域名
    //public static String DEFAULT_HOST="http://jiajie-touch-v2.7dugo.com";//alipays://platformapi/startApp http://192.168.1.200:19021/#/login  http://jiajie-touch.7dugo.com  http://jiajie-touch-v2.7dugo.com
    public static String DEFAULT_HOST="http://jiajie-touch-v2.wangyi120.cn";
    public static String TEST_HOST="http://jiajie-touch-v2.wangyi120.cn";
    public static String apkUrl="http://download.7dugo.com/bangjiajie.apk";
    public static String api_version_check="http://download.7dugo.com/bangjiajieupdate.txt";
    //后台接口域名
    public static String API_HOST= ApiHttpUrl.HOST;
    public static String api_login_by_mobile=API_HOST+"/user.login-msn";
}
