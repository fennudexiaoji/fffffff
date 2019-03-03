package com.common.lib.retrofit.api;


import com.common.lib.bean.FileBean;
import com.common.lib.retrofit.model.BaseResponse;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadAPI {
    /*多文件上传示例*/
    @Multipart
    @POST("zuul/topic/saveTopic")
    Observable<BaseResponse> uploadMultipartFile(@Body RequestBody requestBody);


    /*单个文件上传示例*/
    @Multipart
    @POST("UploadServlet")
    Observable<BaseResponse> uploadSingleFile(@Part("description") RequestBody description,
                                              @Part MultipartBody.Part file);



    @Multipart
    @POST("UploadServlet")
    Flowable<BaseResponse> uploads(@Part ArrayList<MultipartBody.Part> files);



    @Multipart
    @POST("upload.image")
    Flowable<BaseResponse<FileBean>> upload(@Part MultipartBody.Part file);

    @Multipart
    @POST("upload.image")
    Flowable<BaseResponse<FileBean>> upload(@PartMap Map<String, RequestBody> map,@Part MultipartBody.Part file);

    @Multipart
    @POST("zuul/topic/saveTopic")
    Flowable<BaseResponse> uploadFiles(@PartMap Map<String, RequestBody> map, @Part ArrayList<MultipartBody.Part> files);

}
