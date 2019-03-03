package com.app.base.presenter;

import com.app.base.api.GoodsImplAPI;
import com.app.base.contract.GoodsContract;
import com.common.lib.base.presenter.BasePresenter;
import com.common.lib.retrofit.rxandroid.LoadingTransformer;

import io.reactivex.disposables.Disposable;


public class GoodsPresenterImpl extends BasePresenter<GoodsContract.View> implements GoodsContract.Presenter{
    public final String TAG = GoodsPresenterImpl.class.getSimpleName();//每个网络请求唯一TAG，用于取消网络请求使用


    @Override
    public void getNeedList(int page) {
        Disposable disposable=GoodsImplAPI.getNeedList(page).subscribe(data -> mView.getNeedListResult(data), throwable -> {
            mView.onError(throwable, true);
            mView.showError(throwable);
        });
        addDisposable("getNeedList",disposable);
    }

    @Override
    public void getOrderList(String json) {
        Disposable disposable=GoodsImplAPI.getUserOrderList(json).subscribe(data -> mView.getOrderListResult(data), throwable -> {
            mView.onError(throwable, true);
            mView.showError(throwable);
        });
        addDisposable("getNeedList",disposable);
    }
}
