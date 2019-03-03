package com.common.lib.retrofit.rxandroid;


import com.common.lib.base.view.ILoadingView;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 是否需要显示进度
 */

public class LoadingFlowableTransformer<T> implements FlowableTransformer<T, T> {
    private ILoadingView mLoading;
    public LoadingFlowableTransformer(@NonNull ILoadingView loading) {
        mLoading = loading;
    }
    @Override
    public Publisher<T> apply(Flowable<T> upstream) {
        /*return upstream.doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {
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
        });*/
        return upstream.doOnSubscribe(new Consumer<Subscription>() {
            @Override
            public void accept(Subscription subscription) throws Exception {
                mLoading.showLoading();
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                mLoading.hiddenLoading();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mLoading.hiddenLoading();
            }
        });
    }
}
