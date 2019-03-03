package com.common.lib.retrofit.rxandroid;
import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.retrofit.model.BaseResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络请求通用设置转换器
 * 组件化的情况下,如果解析失败,请使用JsonArrayParesTransformer,JsonParesTransformer
 *
 * @param <T>
 */
public class ResponseTransformer<T> implements ObservableTransformer<BaseResponse<T>, T> {
    /*private static final int DEFAULT_TIME_OUT = 5;
    private static final int DEFAULT_RETRY = 3;

    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws ExceptionHandle.ResponseThrowable {
            return Observable.error(ExceptionHandle.handleException(throwable));
        }
    }
    private class HandleFuc<T> implements Function<BaseResponse<T>, T> {

        @Override
        public T apply(BaseResponse<T> tBaseResponse) throws APIException {
            if (tBaseResponse.getError()==0) {
                return tBaseResponse.getData();
            }
            throw new APIException(tBaseResponse.getError(), tBaseResponse.getMsg());
        }
    }
    @Override
    public ObservableSource<T> apply(Observable<BaseResponse<T>> upstream) {
        return upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .timeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
                .retry(DEFAULT_RETRY)
                .map(new HandleFuc<T>()).onErrorResumeNext(new HttpResponseFunc<T>());
    }*/

    @Override
    public Observable<T> apply(Observable<BaseResponse<T>> responseObservable) {
        return responseObservable.compose(new FullResponseTransformer<>())
                .flatMap(response -> Observable.just(response.getData()));
    }

}