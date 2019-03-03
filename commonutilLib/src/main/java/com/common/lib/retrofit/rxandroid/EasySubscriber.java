package com.common.lib.retrofit.rxandroid;

import android.content.Context;

import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;


public abstract class EasySubscriber<T> implements Subscriber<T> {

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
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        if(throwable instanceof APIException){
            APIException apiException= (APIException) throwable;
            _onError(apiException.code,apiException.message);
        }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
            ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
            _onError(responseThrowable.code,responseThrowable.message);
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
        mSubscription.request(1);
    }


    protected abstract void _onNext(T t);
    protected abstract void _onError(int errorCode,String msg);

}
