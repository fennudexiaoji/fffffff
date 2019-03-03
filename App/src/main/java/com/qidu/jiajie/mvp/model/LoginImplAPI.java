package com.qidu.jiajie.mvp.model;

import com.app.base.bean.StaffUser;
import com.app.base.bean.StoreOwnBean;
import com.app.base.bean.UserRealm;
import com.common.lib.retrofit.RetrofitUtils;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.retrofit.rxandroid.FullResponseTransformer;
import com.common.lib.retrofit.rxandroid.ResponseTransformer;
import com.qidu.jiajie.bean.Auth;
import com.qidu.jiajie.bean.WXAuthEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 首页
 */

public class LoginImplAPI {
    //获取首页服务列表
    public static Observable<UserRealm>  getTokenByLogin(String mobile, String verify_code){
        LoginAPI loginAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<UserRealm>> observable = loginAPI.getTokenByLogin(mobile,verify_code);
        return observable.compose(new ResponseTransformer());
    }

    //
    public static Observable<UserRealm>  getUserInfo(){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<UserRealm>> observable = userInfoAPI.getUserInfo("");
        return observable.compose(new ResponseTransformer());
    }

    public static Observable<BaseResponse> getCode(String user_phone){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<List<String>>> observable = userInfoAPI.getCode(user_phone);
        return observable.compose(new FullResponseTransformer<>());
    }


    public static Observable<UserRealm> loginByAliPay(String auth_code){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<UserRealm>> observable = userInfoAPI.loginByAliPay(auth_code);
        return observable.compose(new ResponseTransformer<>());
    }

    //微信登录
    public static Observable<Auth> getAccessTokenByCode(HashMap<String,String> hashMap){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<Auth> observable = userInfoAPI.getAccessTokenByCode(hashMap);
        return observable;
    }

    public static Observable<WXAuthEntity> getUserInfoFromWX(HashMap<String,String> hashMap){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<WXAuthEntity> observable = userInfoAPI.getUserInfoFromWX(hashMap);
        return observable;
    }

    //loginByWX
    public static Observable<UserRealm> loginByWX(String wx_openid,String wx_nickname,String wx_unionid,String headimgurl){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<UserRealm>> observable = userInfoAPI.loginByWX(wx_openid,wx_nickname,wx_unionid,headimgurl);
        return observable.compose(new ResponseTransformer<>());
    }


    //获取店铺信息 user.store.info.get 无参
    public static Observable<StoreOwnBean>  getStoreInfo(){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<StoreOwnBean>> observable = userInfoAPI.getStoreInfo("");
        return observable.compose(new ResponseTransformer());
    }
    //获取清洁师信息
    public static Observable<StaffUser>  getStaffInfo(){
        LoginAPI userInfoAPI = RetrofitUtils.createService(LoginAPI.class);
        Observable<BaseResponse<StaffUser>> observable = userInfoAPI.getStaffInfo("");
        return observable.compose(new ResponseTransformer());
    }

}

