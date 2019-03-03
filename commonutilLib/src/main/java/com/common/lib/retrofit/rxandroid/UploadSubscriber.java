package com.common.lib.retrofit.rxandroid;

import android.content.Context;

import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.retrofit.model.BaseResponse;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

public abstract class UploadSubscriber<T> implements Subscriber<Object> {

    protected Context mContext;

    public UploadSubscriber(Context context) {
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

        if(o instanceof BaseResponse) {
            BaseResponse baseModel = (BaseResponse) o;
            if(baseModel.getError()!=BaseResponse.SUCCESS_CODE) {
                List<String> listMsg=baseModel.getMsg();
                String msg=listMsg.size()>0?listMsg.get(0).toString():"";
                _onError(baseModel.getError(),/*baseModel.getMsg()*/msg);
            }else {
                if(baseModel.getData() != null) {
                    _onNext((T)baseModel.getData());
                }
            }
        }
        mSubscription.request(1);
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

    protected abstract void _onNext(T result);

    protected abstract void _onProgress(Integer percent);

    protected abstract void _onError(int errorCode,String msg);


}
