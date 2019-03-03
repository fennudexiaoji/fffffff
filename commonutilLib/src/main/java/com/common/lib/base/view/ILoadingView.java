package com.common.lib.base.view;

public interface ILoadingView {
    void showLoading();
    void hiddenLoading();
    default void showEmpty(){}
    default void showError(Throwable throwable){}
    void onSuccess();
}