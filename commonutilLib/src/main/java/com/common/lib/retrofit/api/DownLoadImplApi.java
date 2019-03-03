package com.common.lib.retrofit.api;


/**
 * https://blog.csdn.net/qq_30574785/article/details/79024656
 */

public class DownLoadImplApi {
    private static DownLoadImplApi sInstace = null;

    public static DownLoadImplApi builder() {
        if (sInstace == null) {
            synchronized (DownLoadImplApi.class) {
                sInstace = new DownLoadImplApi();
            }
        }
        return sInstace;
    }



    /*使用示例
    DownLoadImplApi.builder().downLoadFile("app-debug.apk").safeSubscribe(new DownLoadSubscriber(this) {
            @Override
            protected void _onNext(String result) {
                Log.i("retrofit", "onNext=======>"+result);
                //tv.setText("下载成功:"+result);
            }

            @Override
            protected void _onProgress(Integer percent) {

                //Log.i("retrofit","onProgress=======>"+percent);
                //tv.setText("下载中:"+percent);
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                Log.i("retrofit", "onProgress=======>"+msg);
                //tv.setText("下载失败:"+msg);
            }
        });*/
}
