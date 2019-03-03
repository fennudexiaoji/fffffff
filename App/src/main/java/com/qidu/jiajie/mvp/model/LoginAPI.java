package com.qidu.jiajie.mvp.model;


import com.app.base.bean.StaffUser;
import com.app.base.bean.StoreOwnBean;
import com.app.base.bean.UserRealm;
import com.common.lib.retrofit.model.BaseResponse;
import com.qidu.jiajie.bean.Auth;
import com.qidu.jiajie.bean.WXAuthEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

interface LoginAPI {

/*
    @Headers({"Content-Type: application/json","Accept: application/json"})*/
    @FormUrlEncoded
    @POST("/user.login-msn")
    Observable<BaseResponse<UserRealm>> getTokenByLogin(@Field("mobile") String mobile, @Field("verify_code") String verify_code);

    @FormUrlEncoded
    @POST("/user.info.get")
    Observable<BaseResponse<UserRealm>> getUserInfo(@Field("token") String token);
    @FormUrlEncoded
    @POST("user.store.info.get")
    Observable<BaseResponse<StoreOwnBean>> getStoreInfo(@Field("token") String token);

    //获取清洁师资料
    @FormUrlEncoded
    @POST("user.store.statistics")
    Observable<BaseResponse<StaffUser>> getStaffInfo(@Field("token") String token);


    //获取验证码
    @FormUrlEncoded
    @POST("/user.code.send")
    Observable<BaseResponse<List<String>>> getCode(@Field("user_phone") String user_phone);

    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
    @FormUrlEncoded
    @POST("user.login-alipay")
    Observable<BaseResponse<UserRealm>> loginByAliPay(@Field("auth_code") String auth_code);

    //微信请求
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token")
    Observable<Auth> getAccessTokenByCode(@QueryMap Map<String, String> maps);

    //获取微信用户信息
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<WXAuthEntity> getUserInfoFromWX(@QueryMap Map<String, String> maps);

    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
    @FormUrlEncoded
    @POST("user.login-wechat")
    Observable<BaseResponse<UserRealm>> loginByWX(@Field("wx_openid") String wx_openid,@Field("wx_nickname") String wx_nickname,@Field("wx_unionid") String wx_unionid,@Field("head_imgurl") String head_imgurl);

}
