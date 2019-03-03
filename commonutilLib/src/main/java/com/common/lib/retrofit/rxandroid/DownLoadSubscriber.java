package com.common.lib.retrofit.rxandroid;

import android.content.Context;

import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public abstract class DownLoadSubscriber implements Subscriber<Object> {

    protected Context mContext;

    public DownLoadSubscriber(Context context) {
        this.mContext = context;
    }

    protected Subscription mSubscription;

    @Override
    public void onSubscribe(Subscription s) {
        this.mSubscription = s;
        mSubscription.request(1);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(Object o) {
        if (o instanceof Integer) {
            _onProgress((Integer) o);
        }

        if(o instanceof String) {
            _onNext((String) o);
        }
        mSubscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        /*if (!NetworkUtil.isNetworkAvailable(mContext)) {
            _onError(ServerException.ERROR_NETWORK,"网络不可用");
        } else if (e instanceof ServerException) {
            _onError(((ServerException) e).getErrorCode(),e.getMessage());
        } else {
            _onError(ServerException.ERROR_OTHER,e.getMessage());
        }*/
        if(throwable instanceof APIException){
            APIException apiException= (APIException) throwable;
            _onError(apiException.code,apiException.message);
        }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
            ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
            _onError(responseThrowable.code,responseThrowable.message);
        }
    }

    protected abstract void _onNext(String result);

    protected abstract void _onProgress(Integer percent);

    protected abstract void _onError(int errorCode,String msg);

}
