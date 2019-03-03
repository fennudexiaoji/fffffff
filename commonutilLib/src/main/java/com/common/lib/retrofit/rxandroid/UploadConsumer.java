package com.common.lib.retrofit.rxandroid;


import com.common.lib.retrofit.model.BaseResponse;

import java.util.List;

import io.reactivex.functions.Consumer;

public abstract class UploadConsumer<T> implements Consumer<Object> {

    @Override
    public void accept(Object o) throws Exception {
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
    }


    protected abstract void _onNext(T result);

    protected abstract void _onProgress(Integer percent);

    protected abstract void _onError(int errorCode,String msg);
}
