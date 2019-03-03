package com.common.lib.retrofit.rxandroid;

import android.util.Log;

import com.common.lib.R;
import com.common.lib.base.BaseApplication;
import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.utils.LogUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 返回全部
 */

public class FullResponseTransformer <T> implements ObservableTransformer<BaseResponse<T>, BaseResponse<T>> {
    @Override
    public ObservableSource<BaseResponse<T>> apply(Observable<BaseResponse<T>> upstream) {
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                .mainThread())
                .unsubscribeOn(Schedulers.io())
                .flatMap(tBaseResponse -> {
                    if (tBaseResponse == null) {
                        LogUtils.e("response = null");
                        return Observable.error(new Throwable(BaseApplication.getInstance().getString(R.string.default_http_error)));
                    } else if (tBaseResponse.getError() == BaseResponse.SUCCESS_CODE) {//请求成功
                        return Observable.just(tBaseResponse);
                    }else {//请求失败
                        /*Log.i("aaaaaa",tBaseResponse.getError()+"---"+tBaseResponse.toString());
                        if(tBaseResponse.getError()==403){
                            return Observable.error(new APIException(tBaseResponse.getError(),""));
                        }*/
                        List<String> listMsg=tBaseResponse.getMsg();
                        String msg=listMsg.size()>0?listMsg.get(0).toString():"";
                        return Observable.error(new APIException(tBaseResponse.getError(),msg));
                        //{"append":[],"count":2,"data":{"rule_controller":"Message","rule_action":"getUnReadCount"},"error":403,"msg":["找不到资源节点"]}
                        //throw new APIException(tBaseResponse.getError(), /*tBaseResponse.getMsg()*/msg);
                    }
                })/*.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends BaseResponse<T>>>() {
                    @Override
                    public ObservableSource<? extends BaseResponse<T>> apply(Throwable throwable) throws Exception {
                        return Observable.error(ExceptionHandle.handleException(throwable));
                    }
                })*/;
    }
}
