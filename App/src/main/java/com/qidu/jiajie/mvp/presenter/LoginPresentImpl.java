package com.qidu.jiajie.mvp.presenter;


import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.app.base.bean.UserRealm;
import com.common.lib.base.BaseApplication;
import com.common.lib.base.presenter.BasePresenter;
import com.common.lib.global.AppGlobal;
import com.common.lib.retrofit.rxandroid.LoadingTransformer;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SharedPreferencesUtils;
import com.common.lib.utils.ToastUtils;
import com.qidu.jiajie.mvp.contract.LoginContract;
import com.qidu.jiajie.mvp.model.LoginImplAPI;
import com.qidu.jiajie.utils.AliPayUtil;
import com.qidu.jiajie.utils.DataUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

public class LoginPresentImpl extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{

    private Activity activity;

    public LoginPresentImpl(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void getTokenByLogin(String mobile, String verify_code) {
        Disposable disposable= LoginImplAPI.getTokenByLogin(mobile,verify_code).compose(new LoadingTransformer<>(mView))
                /*.subscribe(data -> getUserInfo(), throwable -> {
                    mView.onError(throwable, true);
                    mView.showError(throwable);
                });*/
                .subscribe(new Consumer<UserRealm>() {
                    @Override
                    public void accept(UserRealm user) throws Exception {
                        SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_TOKEN,user.getToken());
                        SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_USER_ID,user.getUser_id());
                        getUserInfo();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mView.showError(throwable);
                    }
                });
        addDisposable("getTokenByLogin",disposable);
    }

    //获取用户信息
    public void getUserInfo(){
        Disposable disposable= LoginImplAPI.getUserInfo()
                /*.subscribe(data ->mView.tokenByLoginResult(data), throwable -> {
                    mView.onError(throwable, true);
                    mView.showError(throwable);
                });*/
                .subscribe(new Consumer<UserRealm>() {
                    @Override
                    public void accept(UserRealm userRealm) throws Exception {
                        if(userRealm!=null){
                            /*userRealm.setNeed_bind_phone(user.getNeed_bind_phone());
                            userRealm.setToken(user.getToken());*/
                            userRealm.getUser_pic();
                            UserRealm.insertUserRealm(userRealm, new Realm.Transaction.OnSuccess() {
                                @Override
                                public void onSuccess() {
                                    mView.tokenByLoginResult(userRealm);
                                }
                            }, new Realm.Transaction.OnError() {
                                @Override
                                public void onError(Throwable error) {
                                    mView.showError(error);
                                }
                            });
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show("登录失败");
                    }
                });
        addDisposable("getUserInfo",disposable);
    }

    @Override
    public void getCode(String user_mobile){
        Disposable disposable= LoginImplAPI.getCode(user_mobile).compose(new LoadingTransformer<>(mView))
                .subscribe(data ->mView.getCodeSuccess(), throwable -> {
                    mView.onError(throwable, true);
                    mView.showError(throwable);
                });
        addDisposable("getCode",disposable);
    }


    @Override
    public void loginByAliPay() {
        AliPayUtil.getInstance(activity).authV2("", new AliPayUtil.AliPayAuth2CallBack() {
            @Override
            public void authSuccess(String result) {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    String authCode=jsonObject.getString("auth_code");
                    aliPayLogin(authCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void aliPayLogin(String auth_code){
        LogUtils.i("aaaaaa","支付宝授权码："+auth_code);
        Disposable disposable=LoginImplAPI.loginByAliPay(auth_code).subscribe(new Consumer<UserRealm>() {
            @Override
            public void accept(UserRealm user) throws Exception {
                SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_TOKEN,user.getToken());
                SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_USER_ID,user.getUser_id());
                getUserInfo();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showError(throwable);
            }
        });
        addDisposable("aliPayLogin",disposable);
    }

    @Override
    public void loginByWX(String wx_openid,String wx_nickname,String wx_unionid,String headimgurl) {
        Disposable disposable=LoginImplAPI.loginByWX(wx_openid,wx_nickname,wx_unionid,headimgurl).subscribe(new Consumer<UserRealm>() {
            @Override
            public void accept(UserRealm user) throws Exception {
                SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_TOKEN,user.getToken());
                SharedPreferencesUtils.getInstance(BaseApplication.getInstance()).saveData(AppGlobal.KEY_LOGIN_USER_ID,user.getUser_id());
                getUserInfo();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                mView.showError(throwable);
            }
        });
        addDisposable("loginByWX",disposable);
    }
}
