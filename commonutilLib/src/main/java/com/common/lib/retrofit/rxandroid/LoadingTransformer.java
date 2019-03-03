package com.common.lib.retrofit.rxandroid;


import com.common.lib.base.view.ILoadingView;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class LoadingTransformer<T> implements ObservableTransformer<T, T> {
    private ILoadingView mLoading;

    public LoadingTransformer(@NonNull ILoadingView loading) {
        mLoading = loading;
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        mLoading.showLoading();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mLoading.hiddenLoading();
                    }
                }).doOnNext(new Consumer<T>() {
                    @Override
                    public void accept(T t) throws Exception {
                        mLoading.hiddenLoading();
                    }
                });
    }

}
