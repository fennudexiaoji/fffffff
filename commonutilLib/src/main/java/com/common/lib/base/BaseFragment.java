package com.common.lib.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.common.lib.base.presenter.BasePresenter;
import com.common.lib.base.view.IBaseFragmentView;
import com.common.lib.retrofit.exception.APIException;
import com.common.lib.retrofit.exception.ExceptionHandle;
import com.common.lib.utils.ToastUtils;


/**
 * 封装RecyclerView
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2016/1104/6746.html
 */
public abstract class BaseFragment<T extends BasePresenter> extends AbsBaseFragment
        implements IBaseFragmentView {
    protected Bundle mBundle;
    protected T mPresenter;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mBundle != null) {
            outState.putBundle("bundle", mBundle);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * 绑定activity
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //应该只创建一次Presenter.
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
        mPresenter.onAttach(this);
    }
    /**
     * 运行在onAttach之后
     * 可以接受别人传递过来的参数,实例化对象.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取bundle,并保存起来
        if (savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle("bundle");
        } else {
            mBundle = getArguments() == null ? new Bundle() : getArguments();
        }
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        super.onStart();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (null != mPresenter) {
            if (isVisibleToUser) {
                //相当于Fragment的onResume
                mPresenter.onResume();
            } else {
                //相当于Fragment的onPause
                mPresenter.onPause();
            }
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 初始化Presenter
     *
     * @return
     */
    protected abstract T initPresenter();
    /**
     * 类似Activity的OnBackgress
     * fragment进行回退
     */
    @Override
    public void onBack() {
        getFragmentManager().popBackStackImmediate();
    }

    /**
     * 是否消费返回键
     * @return
     */
    /*@CallSuper
    public boolean onBackPressed() {
        boolean consume = false;
        if(getChildFragmentManager().getFragments() == null){
            return false;
        }
        for(Fragment fragment : getChildFragmentManager().getFragments()){
            if(fragment instanceof BaseFragment){
                BaseFragment baseFragment = (BaseFragment)fragment;
                consume = baseFragment.onBackPressed();
                if(consume){
                    break;
                }
            }
        }

        return consume;
    }*/

    /**
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
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.setCustomAnimations(
//                R.anim.translate_fade_in_right,
//                R.anim.translate_fade_out_right, R.anim.translate_fade_in_left, R.anim.translate_fade_out_left);
        fragmentTransaction.hide(this).add(android.R.id.content, tofragment, tag);
        fragmentTransaction.addToBackStack(tag);
        fragmentTransaction.commitAllowingStateLoss();
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
                if(apiException.code==401){
                    //登录
                }
            }else if(throwable instanceof ExceptionHandle.ResponseThrowable){
                ExceptionHandle.ResponseThrowable responseThrowable= (ExceptionHandle.ResponseThrowable) throwable;
                ToastUtils.show(responseThrowable.message);
            }*/

            ExceptionHandle.ResponseThrowable responseThrowable=ExceptionHandle.handleException(throwable);
            ToastUtils.show(responseThrowable.message);
        }
    }
    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hiddenLoading() {
        dismissProgressDialog();
    }
}
