package com.qidu.jiajie.mvp.model;


import com.app.base.bean.AppInfo;
import com.app.base.bean.BaseResponseBean;
import com.common.lib.retrofit.model.BaseResponse;
import com.qidu.jiajie.bean.Notice;
import com.qidu.jiajie.bean.ServiceEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

interface HomeAPI {


    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("category.list")
    Observable<BaseResponse<List<ServiceEntity>>> loadServiceList(@Body RequestBody body);

    //未读消息
    @FormUrlEncoded
    @POST("message.unread")
    Observable<BaseResponse<Notice>> getNoticeUnRead(@Field("token") String token);

    @GET()
    Observable<BaseResponseBean<AppInfo>> checkAppOnLineVersion(@Url() String url);


    /*@FormUrlEncoded
    @POST("FundPaperTrade/AppUserLogin")
    Observable<Response> getTransData(@FieldMap Map<String,String> map);*/
}
