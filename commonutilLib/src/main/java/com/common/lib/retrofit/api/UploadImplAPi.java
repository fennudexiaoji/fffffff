package com.common.lib.retrofit.api;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import com.common.lib.base.BaseApplication;
import com.common.lib.bean.FileBean;
import com.common.lib.retrofit.RetrofitManager;
import com.common.lib.retrofit.RetrofitUtils;
import com.common.lib.retrofit.body.UploadRequestBody;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.retrofit.rxandroid.ResponseTransformer;
import com.common.lib.retrofit.rxandroid.UploadConsumer;
import com.common.lib.retrofit.rxandroid.UploadOnSubscribe;
import com.common.lib.retrofit.rxandroid.UploadSubscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadImplAPi {
    public static Observable<BaseResponse> uploads(int type, @NonNull String content, @Nullable List<File> photos){
        List<Pair<String, String>> params = new ArrayList<>(3);
        params.add(new Pair<>("topicTypeId", String.valueOf(type)));
        params.add(new Pair<>("title", "标题"));
        params.add(new Pair<>("content", content));

        List<Pair<String, File>> files = new ArrayList<>();
        if(photos != null) {
            int i=0;
            for (File file : photos) {
                files.add(new Pair<>("multipart" + i, file));
                i++;
            }
        }

        RequestBody requestBody = RetrofitManager.getInstance().createRequestBody(params, files);
        UploadAPI communityAPI = RetrofitUtils.createService(UploadAPI.class);
        Observable<BaseResponse> observable = communityAPI.uploadMultipartFile(requestBody);
        return observable.compose(new ResponseTransformer());
    }


    public static Observable<BaseResponse> uploadSingleFile(File file){
        String descriptionString = "description";
        RequestBody description =
                RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part requestBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        UploadAPI communityAPI = RetrofitUtils.createService(UploadAPI.class);
        Observable<BaseResponse> observable = communityAPI.uploadSingleFile(description,requestBody);
        return observable.compose(new ResponseTransformer());
    }


    //建议使用方式，避免原来反射方法获取方法名
    public static <T> Flowable<Object> uploadFile(File file) {
//      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(file.length());
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);
        UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
//      设置进度监听
        uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);
//      创建表单主体
        MultipartBody.Part filePart =
                MultipartBody.Part.createFormData("image", file.getName(), uploadRequestBody);
        UploadAPI communityAPI = RetrofitUtils.createService(UploadAPI.class);
        Flowable<BaseResponse<FileBean>> uploadFlowable =communityAPI.upload(filePart);
//              合并Observable  progressObservale返回int uploadFlowable object
        return Flowable.merge(progressObservale, uploadFlowable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Flowable<Object> uploadFiles(HashMap<String,String> hashMap, @Nullable List<File> photos) {
        long sumLength = 0l;
        if(photos != null) {
            for (File file : photos) {
                sumLength += file.length();
            }
        }
        //      进度Observable
        UploadOnSubscribe uploadOnSubscribe = new UploadOnSubscribe(sumLength);
        Flowable<Integer> progressObservale = Flowable.create(uploadOnSubscribe, BackpressureStrategy.BUFFER);
        ArrayList<MultipartBody.Part> fileParts = new ArrayList<>();
        for (File file : photos) {
            UploadRequestBody uploadRequestBody = new UploadRequestBody(file);
//          设置进度监听
            uploadRequestBody.setUploadOnSubscribe(uploadOnSubscribe);
            fileParts.add(MultipartBody.Part.createFormData("image", file.getName(), uploadRequestBody));
        }
        UploadAPI uploadAPI = RetrofitUtils.createService(UploadAPI.class);
        Map<String, RequestBody> map=new HashMap<>();
        if(hashMap!=null) {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), entry.getValue());
                map.put(entry.getKey(), requestBody);
            }
        }
        Flowable<BaseResponse> uploadFlowable =uploadAPI.uploadFiles(map,fileParts);
//              合并Observable
        return Flowable.merge(progressObservale, uploadFlowable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public static void test(){
        String path="/storage/emulated/0/Android/data/app.odp.qidu/cache/1533714092626.png";
        File file=new File(path);
        //uploadFile(file).compose()
        Disposable disposable=uploadFile(file)
                .subscribe(new UploadConsumer<FileBean>() {
                    @Override
                    protected void _onNext(FileBean result) {
                        Log.i("vvvvv", "result----->" + result.getPath());
                    }
                    @Override
                    protected void _onProgress(Integer percent) {
                        Log.i("vvvvv", "percent----->" + percent);
                    }
                    @Override
                    protected void _onError(int errorCode, String msg) {
                        Log.i("vvvvv", "errorCode----->" + errorCode);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i("vvvvv", "throwable----->" + throwable.getMessage());
                    }
                });
        /*Disposable d = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(23);
                System.out.println(23);
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.MINUTES)
                .subscribe(integer -> {
                    System.out.println(integer);


                });*/



    }

    /**
     * 单图上传
     */
    public void upload() {
        String path="/storage/emulated/0/Android/data/app.odp.qidu/cache/1533714092626.png";
        File file=new File(path);
        RetrofitUtils.uploadFile(file, UploadAPI.class, "upload")
                //.compose(new LoadingFlowableTransformer<Object>(mView))
                //注意解析写法 Flowable<BaseResponse<FileBean>> upload(@Part MultipartBody.Part file);
        .subscribeWith(new UploadSubscriber<FileBean>(BaseApplication.getInstance()) {
            @Override
            protected void _onNext(FileBean result) {
                Log.i("retrofit", "onNext=======>url:"+result.getPath());
                //tv.setText("上传成功:"+result.getUrl());
            }

            @Override
            protected void _onProgress(Integer percent) {
                Log.i("retrofit", "onProgress======>"+percent);
                //tv.setText("上传中:"+percent);
            }

            @Override
            protected void _onError(int errorCode, String msg) {
                Log.i("retrofit", "onError======>"+msg);
                //tv.setText("上传失败:"+msg);
            }
        });
                /*.safeSubscribe(new UploadSubscriber<FileBean>(BaseApplication.getInstance()) {
                    @Override
                    protected void _onNext(FileBean result) {
                        Log.i("retrofit", "onNext=======>url:"+result.getPath());
                        //tv.setText("上传成功:"+result.getUrl());
                    }

                    @Override
                    protected void _onProgress(Integer percent) {
                        Log.i("retrofit", "onProgress======>"+percent);
                        //tv.setText("上传中:"+percent);
                    }

                    @Override
                    protected void _onError(int errorCode, String msg) {
                        Log.i("retrofit", "onError======>"+msg);
                        //tv.setText("上传失败:"+msg);
                    }
                });*/
    }




    /**
     * 多图上传
     */
    /*public void uploads(ArrayList<File> files) {
        RetrofitUtils.getInstance().uploadFiles(files, UploadAPI.class, "uploads")
                .safeSubscribe(new UploadSubscriber<ArrayList<FileBean>>(this) {
                    @Override
                    protected void _onNext(ArrayList<FileBean> result) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (FileBean fileBean : result) {
                            stringBuilder.append("上传成功:"+fileBean.getUrl()+"\n");
                        }
                        Log.i("retrofit", "onNext=======>"+stringBuilder);
                        tv.setText("上传成功:"+stringBuilder);
                    }

                    @Override
                    protected void _onProgress(Integer percent) {
                        Log.i("retrofit", "onProgress=======>"+percent);
                        tv.setText("上传中:"+percent);
                    }

                    @Override
                    protected void _onError(int errorCode, String msg) {
                        Log.i("retrofit", "onError======>"+msg);
                        tv.setText("上传失败:"+msg);
                    }
                });
    }*/

}
