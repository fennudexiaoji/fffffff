package com.common.lib.retrofit.rxandroid;

import com.common.lib.retrofit.factory.JSONFactory;
import com.common.lib.retrofit.model.BaseResponse;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


public class JsonParesTransformer<T> implements ObservableTransformer<String, T> {
    private final Class<T> zClass;

    public JsonParesTransformer(Class<T> zClass) {
        this.zClass = zClass;
    }

    /*@Override
    public ObservableSource<T> apply(Observable<BaseResponse<String>> upstream) {
        return upstream.compose(new ResponseTransformer<String>())
                .observeOn(Schedulers.computation())
                .flatMap(new Function<String, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(String s) throws Exception {
                        return Observable.just(JSONFactory.fromJson(s, zClass));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }*/

    @Override
    public ObservableSource<T> apply(Observable<String> upstream) {
        return upstream.observeOn(Schedulers.computation())
                .flatMap(new Function<String, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(String s) throws Exception {
                        return Observable.just(JSONFactory.fromJson(s, zClass));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
}
