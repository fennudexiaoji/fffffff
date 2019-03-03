package com.common.lib.retrofit;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by 7du-28 on 2018/9/17.
 */

public class RxApiManager implements RxActionManager<Object> {
    private static RxApiManager sInstance = null;

    private ArrayMap<Object, Disposable> maps;
    private CompositeDisposable compositeDisposable;


    //全局单一实例
    public static RxApiManager get() {
        if (sInstance == null) {
            synchronized (RxApiManager.class) {
                if (sInstance == null) {
                    sInstance = new RxApiManager();
                }
            }
        }
        return sInstance;
    }
    //每个页面一个示例，取消时只取消临时对象的全部请求

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private RxApiManager() {
        maps = new ArrayMap<>();
        compositeDisposable=new CompositeDisposable();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void add(Object tag, Disposable disposable) {
        compositeDisposable.add(disposable);
        maps.put(tag, disposable);
    }

    //同一个tag只能有一个请求的情况
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void addAlone(Object tag, Disposable disposable) {
        cancel(tag);
        add(tag,disposable);
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void remove(Object tag) {
        if (!maps.isEmpty()) {
            compositeDisposable.remove(maps.get(tag));
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void removeAll() {
        if (!maps.isEmpty()) {
            compositeDisposable.clear();
            maps.clear();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            compositeDisposable.remove(maps.get(tag));
            maps.remove(tag);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }

}
