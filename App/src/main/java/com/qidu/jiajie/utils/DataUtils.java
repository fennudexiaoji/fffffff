package com.qidu.jiajie.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.amap.api.services.core.PoiItem;
import com.common.lib.retrofit.cookie.CookieStore;
import com.common.lib.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.List;

import okhttp3.Cookie;


public class DataUtils {
    public static String WECHAT_APP_ID="wx9aed778052cef169";
    public static final String WECHAT_SECRET_ID="c9e56a1a65f1e5f4b04c7cd1196e3781";
    /*public static String WECHAT_APP_ID="wx6c69af15244da7bc";
    public static final String WECHAT_SECRET_ID="b9bd023014450e2fd4d8b6a97424be15";*/





    public static String getAddressData(PoiItem poiSelectedItem){
        //final String str="地址:"+selected.getSnippet()+"纬度:"+selected.getLatLonPoint().getLatitude()+"经度:"+selected.getLatLonPoint().getLongitude();
        //final String str=poiSelectedItem.getProvinceName()+poiSelectedItem.getCityName()+poiSelectedItem.getDirection()+poiSelectedItem.getSnippet()+poiSelectedItem.getTitle();
        final JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("provinceName",poiSelectedItem.getProvinceName());
            jsonObject.put("cityName",poiSelectedItem.getCityName());
            jsonObject.put("cityCode",poiSelectedItem.getCityCode());
            jsonObject.put("adCode",poiSelectedItem.getAdCode());
            jsonObject.put("district",poiSelectedItem.getAdName());
            //jsonObject.put("snippet",poiSelectedItem.getSnippet());
            jsonObject.put("title",poiSelectedItem.getTitle());
            jsonObject.put("latitude",poiSelectedItem.getLatLonPoint().getLatitude());
            jsonObject.put("longitude",poiSelectedItem.getLatLonPoint().getLongitude());
            jsonObject.put("address",poiSelectedItem.getSnippet());
            //Log.i("aaaaaaa",jsonObject.toString());
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    /*同步cookie*/
    public static void synCookies(Context context, WebView webView,String url) {
        //CookieSyncManager负责管理webView中的cookie
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.acceptCookie();
        CookieStore cookieStore=new CookieStore(context);
        List<Cookie> cookieList=cookieStore.getCookies();
        //  根据版本不同,用不同方法刷新删除之前的cookie
        //  根据版本不同,用不同方法刷新cookie
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
            cookieManager.removeSessionCookies(null);
            cookieManager.removeAllCookies(null);

            String cookies = cookieList.toString();
            // 只截取中间的cookie  主要就是这
            String cookie = cookies.substring(1, cookies.length() - 1);
            LogUtils.i("缓存的cookie----", cookie);
            cookieManager.setCookie(url,cookie);
            cookieManager.flush();
        } else {
            // 清理之前的缓存
            cookieManager.removeSessionCookie();
            cookieManager.removeAllCookie();
            for (int i = 0; i < cookieList.size(); i++) {
                Cookie cookie = cookieList.get(i);
                if (cookie.name().equals("token")) {
                    cookieManager.setCookie(url, cookie.name() + "=" + cookie.value());
                }
            }
            CookieSyncManager.createInstance(context).sync();
        }
        String newCookie = cookieManager.getCookie(url);
        LogUtils.i("同步后cookie", newCookie);
    }




}
