package com.qidu.jiajie.mvp.apiservice;


import com.common.lib.retrofit.model.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 */
public interface APIService {

    /*
    注解解釋
    https://www.jianshu.com/p/3e13e5d34531
    @Url：使用全路径复写baseUrl，适用于非统一baseUrl的场景。
    *@Streaming:用于下载大文件
    * */


    /**
     * 在这里写请求接口
     */
    @GET("/store_api-020")
    Observable<BaseResponse<List<String>>> login(@QueryMap HashMap<String, String> params);

    /**
     * h5交互获取页面数据.
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return
     */
    @GET
    Observable<BaseResponse<String>> loadWebViewData(@Url String url, @QueryMap HashMap<String, Object> params);


    @Multipart
    @POST("{url}")
    <T> Observable<T> uploadFiles(
            @Path("url") String url,
            @PartMap() Map<String, RequestBody> maps);

    @Multipart
    @POST("{url}")
    Observable<ResponseBody> uploadFiles(
            @Path("url") String url,
            //@Path("headers") Map<String, String> headers,
            @Part("description") String description,//文件描述
            @PartMap() Map<String, RequestBody> maps);

    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);


    /*post请求示例*/
    //@Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("{url}")
    Observable<ResponseBody> executePost(
            @Path("url") String url,
            //  @Header("") String authorization,
            @QueryMap Map<String, String> maps);
}
