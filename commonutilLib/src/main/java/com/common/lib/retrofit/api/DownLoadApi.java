package com.common.lib.retrofit.api;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * https://blog.csdn.net/qq_30574785/article/details/79024656
 */

public interface DownLoadApi {
    /**
     * 下载文件
     *
     * @param fileUrl
     * @return
     */
    @Streaming //大文件时要加不然会OOM
    @GET
    Flowable<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
