package com.common.lib.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.common.lib.base.presenter.BasePresenter;
import com.common.lib.base.view.IBaseActivityView;
import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.utils.ToastUtils;


public abstract class BaseActivity<T extends BasePresenter> extends AbsBaseActivity
        implements IBaseActivityView {
    protected T mPresenter;
    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hiddenLoading() {
        dismissProgressDialog();
    }

    /**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建presenter
        mPresenter = initPresenter();
        //绑定Activity
        mPresenter.onAttach(this);
    }

    /**
     * 子类实现Presenter,且必须继承BasePresenter
     *
     * @return
     */
    protected abstract T initPresenter();
    @Override
    protected void onStart() {
        mPresenter.onStart();
        super.onStart();
    }

    @Override
    protected void onPause() {
        mPresenter.onPause();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        mPresenter.onRestart();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        mPresenter.onResume();
        super.onResume();
    }
    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    /**
     * '
     * 跳转fragment
     *
     * @param tofragment
     */
    @Override
    public void startFragment(Fragment tofragment) {
        startFragment(tofragment, null);
    }

    /**
     * @param tofragment 跳转的fragment
     * @param tag        fragment的标签
     */
    @Override
    public void startFragment(Fragment tofragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(android.R.id.content, tofragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * onBackPressed();
     */
    @Override
    public void onBack() {
        onBackPressed();
    }

    @Override
    public BaseActivity getActivity() {
        return this;
    }


    @Override
    public void showEmpty() {
        //root.showEmpty();
    }

    @Override
    public void showError(Throwable throwable) {
        /*if(throwable instanceof APIException){
            APIException apiException= (APIException) throwable;
            root.showError(apiException.message);
        }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
            ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
            root.showError(responseThrowable.message);
        }else {
            root.showError();
        }*/
    }


    @Override
    public void showTips(int tipRes) {
        ToastUtils.show(getResources().getString(tipRes));
    }

    @Override
    public void showTips(String tip) {
        ToastUtils.show(tip);
    }

    @Override
    public void onSuccess() {
        //root.showContent();
    }

    @Override
    public void onError(Throwable throwable, boolean show) {
        if(show){
            /*if(throwable instanceof APIException){
                APIException apiException= (APIException) throwable;
                ToastUtils.show(apiException.message);
            }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
                ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
                ToastUtils.show(responseThrowable.message);
            }*/
            ExceptionHandle.ResponseThrowable responseThrowable=ExceptionHandle.handleException(throwable);
            ToastUtils.show(responseThrowable.message);
        }
    }
}
