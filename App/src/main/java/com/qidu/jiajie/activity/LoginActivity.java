package com.qidu.jiajie.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.base.bean.UserRealm;
import com.app.base.utils.CommonKey;
import com.app.base.utils.IntentParams;
import com.common.lib.base.BaseActivity;
import com.common.lib.utils.ToastUtils;
import com.google.gson.Gson;
import com.qidu.jiajie.R;
import com.qidu.jiajie.bean.WXAuthEntity;
import com.qidu.jiajie.databinding.ActivityLoginBinding;
import com.qidu.jiajie.mvp.contract.LoginContract;
import com.qidu.jiajie.mvp.presenter.LoginPresentImpl;
import com.qidu.jiajie.utils.DataUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class LoginActivity extends BaseActivity<LoginPresentImpl> implements LoginContract.View{
    private ActivityLoginBinding binding;

    //定义支付域名（替换成公司申请H5的域名即可）
    BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(IntentParams.ACTION_GET_USER_INFO_BY_WX_LOGIN)){
                String response=intent.getStringExtra(IntentParams.KEY_USER_INFO_BY_WX_LOGIN);
                Gson gson=new Gson();
                WXAuthEntity wxAuthEntity=gson.fromJson(response,WXAuthEntity.class);
                mPresenter.loginByWX(wxAuthEntity.getOpenid(),wxAuthEntity.getNickname(),wxAuthEntity.getUnionid(),wxAuthEntity.getHeadimgurl());
            }
        }
    };
    @Override
    public void tokenByLoginResult(UserRealm user) {
        ToastUtils.show("登录成功");
        getActivity().finish();
    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void getCodeError() {

    }

    @Override
    protected LoginPresentImpl initPresenter() {
        return new LoginPresentImpl(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if(!getActivity().getPackageName().equals("com.qidu.jiajie")){
            LoginByMobileFragment loginByMobileFragment=LoginByMobileFragment.getInstance(CommonKey.KEY_LOGIN_WORKER_OR_SELLER);
            startFragment(loginByMobileFragment);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentParams.ACTION_GET_USER_INFO_BY_WX_LOGIN);
        registerReceiver(broadcastReceiver, intentFilter);
        binding.layoutTitle.headerLeftBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_back));
        binding.layoutTitle.headerLeftBtnImg.setOnClickListener(v -> {
            finish();
        });
        binding.layoutTitle.headerText.setText("登录");
        binding.mobileLogin.setOnClickListener(v -> {
            LoginByMobileFragment loginByMobileFragment=LoginByMobileFragment.getInstance("user");
            startFragment(loginByMobileFragment);
        });

        binding.wechatLogin.setOnClickListener(v -> {
            IWXAPI api = WXAPIFactory.createWXAPI(this, DataUtils.WECHAT_APP_ID);
            api.registerApp(DataUtils.WECHAT_APP_ID);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wx_login";
            api.sendReq(req);
        });
        binding.alipayLogin.setOnClickListener(v -> {
            mPresenter.loginByAliPay();
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
