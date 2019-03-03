package com.common.lib.base.view;

import android.support.annotation.StringRes;


public interface IBaseView<T> extends ILoadingView {
    void showTips(@StringRes int tipRes);
    void showTips(String tip);
    void onError(Throwable throwable, boolean show);


}
