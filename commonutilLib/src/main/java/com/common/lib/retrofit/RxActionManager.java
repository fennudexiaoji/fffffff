package com.common.lib.retrofit;

import io.reactivex.disposables.Disposable;

/**
 * Created by 7du-28 on 2018/9/17.
 */

public interface RxActionManager<T> {

    void add(T tag, Disposable disposable);

    //同一个tag只能有一个请求的情况
    void addAlone(T tag, Disposable disposable);

    void remove(T tag);

    void cancel(T tag);

    void cancelAll();
}
