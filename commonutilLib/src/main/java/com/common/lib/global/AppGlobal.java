package com.common.lib.global;

import android.app.Application;
import android.text.TextUtils;

import com.common.lib.base.BaseApplication;
import com.common.lib.utils.SharedPreferencesUtils;

/**
 * Created by 7du-28 on 2018/2/3.
 */

public class AppGlobal {
    public static Application appContext= BaseApplication.getInstance();
    public static String KEY_LOGIN_TOKEN="key.login.token";
    public static String KEY_LOGIN_USER_ID="key.login.user.id";
    public static boolean isLogin(){
        String token= (String) SharedPreferencesUtils.getInstance(appContext).getData(AppGlobal.KEY_LOGIN_TOKEN,"");
        if(!TextUtils.isEmpty(token)) {
            return true;
        }
        return false;
    }
    public static String getToken(){
        /*if(TextUtils.isEmpty(token)){
            token= (String) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(KEY_LOGIN_TOKEN,"");
        }*/
        String token= (String) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(KEY_LOGIN_TOKEN,"");
        return token;
    }

    public static String getUserId(){
        String userId= (String) SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).getData(KEY_LOGIN_USER_ID,"");
        return userId;
    }

}
