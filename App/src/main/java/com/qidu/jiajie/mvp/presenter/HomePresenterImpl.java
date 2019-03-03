package com.qidu.jiajie.mvp.presenter;


import android.app.Activity;
import android.content.Context;

import com.app.base.bean.AppInfo;
import com.app.base.bean.BaseResponseBean;
import com.common.lib.base.presenter.BasePresenter;
import com.common.lib.retrofit.rxandroid.LoadingTransformer;
import com.qidu.jiajie.mvp.contract.HomeContract;
import com.qidu.jiajie.mvp.model.HomeImplAPI;
import com.qidu.jiajie.utils.VersionUpdateUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 首页
 */

public class HomePresenterImpl extends BasePresenter<HomeContract.View> implements HomeContract.Presenter{

    private Activity activity;

    public HomePresenterImpl(Activity context) {
        this.activity = context;
    }

    @Override
    public void loadServiceList(int page, String jsonParam) {
        Disposable disposable= HomeImplAPI.getServiceList().compose(new LoadingTransformer<>(mView))
                .subscribe(data -> mView.getServiceList(data), throwable -> {
                    mView.onError(throwable, true);
                    mView.showError(throwable);
                });
        addDisposable("loadServiceList",disposable);
    }

    @Override
    public void checkAppOnLineVersion(String url) {
        Disposable disposable= HomeImplAPI.checkAppOnLineVersion(url).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread())
                .unsubscribeOn(Schedulers.io()).subscribe(new Consumer<BaseResponseBean<AppInfo>>() {
            @Override
            public void accept(BaseResponseBean<AppInfo> appInfoBaseResponseBean) throws Exception {
                VersionUpdateUtil.getInstance(activity).updateVersion(appInfoBaseResponseBean.getData());
            }
        });
        addDisposable("checkAppOnLineVersion",disposable);
    }

}
