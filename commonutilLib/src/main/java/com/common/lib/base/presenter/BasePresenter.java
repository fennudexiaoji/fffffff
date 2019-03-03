package com.common.lib.base.presenter;


import android.content.Intent;
import android.os.Bundle;

import com.common.lib.base.view.IBaseView;
import com.common.lib.retrofit.RxApiManager;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter <T extends IBaseView>{

    protected T mView;
    protected RxApiManager rxApiManager;
    /**
     * 绑定View
     */
    public void onAttach(T view) {
        this.mView = view;
        rxApiManager=RxApiManager.get();
    }
    public void addDisposable(Object tag, Disposable disposable){
        if(rxApiManager!=null){
            rxApiManager.add(tag,disposable);
        }
    }
    public void addDisposableAlone(Object tag, Disposable disposable){
        if(rxApiManager!=null){
            rxApiManager.addAlone(tag,disposable);
        }
    }
    /**
     * 在这里结束异步操作
     */
    public void onDestroy() {
        mView = null;
        //取消页面的网络请求
        if(rxApiManager!=null){
            rxApiManager.removeAll();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onSaveInstanceState(Bundle outState) {

    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onRestart() {

    }

    public void onPause() {

    }

    public void onStop() {

    }
}
