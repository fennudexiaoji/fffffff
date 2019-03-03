package com.common.lib.retrofit;


import com.common.lib.base.BaseApplication;
import com.common.lib.fileutils.FileUtils;
import com.common.lib.retrofit.api.DownLoadApi;
import com.common.lib.retrofit.body.UploadRequestBody;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.retrofit.rxandroid.DownLoadTransformer;
import com.common.lib.retrofit.rxandroid.UploadOnSubscribe;

import org.reactivestreams.Publisher;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class RetrofitUtils {

    /*private static RetrofitUtils retrofitUtils;
    public static RetrofitUtils getInstance() {
        RetrofitUtils _retrofitUtils = retrofitUtils;
        if (retrofitUtils == null) {
            synchronized (RetrofitManager.class) {
                _retrofitUtils = retrofitUtils;
                if (retrofitUtils == null) {
                    _retrofitUtils = new RetrofitUtils();
                    retrofitUtils = _retrofitUtils;
                }
            }
        }
        return _retrofitUtils;
    }*/

    /**
     * 创建请求
     */
    public static <T> T createService(final Class<T> service) {
        return RetrofitManager.getInstance().getRetrofit().create(service);
    }

    public <S> S createServer(Class<S> serverClass, String baseUrl) {
        return RetrofitManager.getInstance().createServer(serverClass,baseUrl);
    }



    /**
     * 上传单个文件
     */
    public static <T> Flowable<Object> uploadFile(File file, Class<T> uploadServiceClass, String uploadFucntionName, Object... params) {
//      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(file.length());
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);

        UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
//      设置进度监听
        uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);
//      创建表单主体
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("image", file.getName(), uploadRequestBody);
//      上传
        T service = createService(uploadServiceClass);
        try {
//            获得上传方法的参数类型   和参数
            Class[] paramClasses = new Class[params.length + 1];
            Object[] uploadParams = new Object[params.length + 1];
            paramClasses[params.length] = MultipartBody.Part.class;
            uploadParams[params.length] = filePart;
            for (int i = 0; i < params.length; i++) {
                paramClasses[i] = params[i].getClass();
                uploadParams[i] = params[i];
            }

//            获得上传方法
            Method uploadMethod = uploadServiceClass.getMethod(uploadFucntionName, paramClasses);

//            运行上传方法
            Object o = uploadMethod.invoke(service, uploadParams);
            if (o instanceof Flowable) {
                Flowable uploadFlowable = (Flowable) o;

//              合并Observable
                return Flowable.merge(progressObservale, uploadFlowable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Flowable.error(e);
        }
        return Flowable.error(new ExceptionHandle.ServerException("no upload method found or api service error", 1002));
    }

    /**
     * 上传多个文件
     */
    public static <T> Flowable<Object> uploadFiles(ArrayList<File> files, Class<T> uploadsServiceClass, String uploadFucntionName, Object... params) {
//        总长度
        long sumLength = 0l;
        for (File file : files) {
            sumLength += file.length();
        }

//      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(sumLength);
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);

        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();

        for (File file : files) {

            UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
//          设置进度监听
            uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);

            fileParts.add(MultipartBody.Part.createFormData("upload", file.getName(), uploadRequestBody));
        }

//      上传
        T service = createService(uploadsServiceClass);

        try {
//            获得上传方法的参数类型   和参数
            Class[] paramClasses = new Class[params.length + 1];
            Object[] uploadParams = new Object[params.length + 1];
            paramClasses[params.length] = ArrayList.class;
            uploadParams[params.length] = fileParts;
            for (int i = 0; i < params.length; i++) {
                paramClasses[i] = params[i].getClass();
                uploadParams[i] = params[i];
            }

//            获得上传方法
            Method uploadMethod = uploadsServiceClass.getMethod(uploadFucntionName, paramClasses);

//            运行上传方法
            Object o = uploadMethod.invoke(service, uploadParams);
            if (o instanceof Flowable) {
                Flowable uploadFlowable = (Flowable) o;

//              合并Observable
                return Flowable.merge(progressObservale, uploadFlowable)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Flowable.error(e);
        }
        return Flowable.error(new ExceptionHandle.ServerException("no upload method found or api service error", 1002));
    }




    /**
     * 下载文件
     */
    public static Flowable<Object> downLoadFile(String url) {
        return downLoadFile(url, null, null);
    }

    /**
     * 下载文件
     */
    public static Flowable<Object> downLoadFile(String url, String savePath, String fileName) {
        if (savePath == null || savePath.trim().equals("")) {
            savePath = FileUtils.getDiskCacheDir(BaseApplication.getInstance()).getPath();
        }
        if (fileName == null || fileName.trim().equals("")) {
            fileName = FileUtils.getDefaultDownLoadFileName(url);
        }
//        下载监听
        DownLoadTransformer downLoadTransformer = new DownLoadTransformer(savePath, fileName);

        return Flowable
                .just(url)
                .flatMap(new Function<String, Publisher<ResponseBody>>() {
                    @Override
                    public Publisher<ResponseBody> apply(@NonNull String s) throws Exception {
                        DownLoadApi downLoadService = createService(DownLoadApi.class);
                        return downLoadService.downloadFileWithDynamicUrlSync(s);
                    }
                })
                .compose(downLoadTransformer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io());
    }





}
