package com.qidu.jiajie.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.bean.UserRealm;
import com.app.base.utils.CommonKey;
import com.common.lib.base.BaseFragment;
import com.common.lib.utils.RegexUtils;
import com.common.lib.utils.ToastUtils;
import com.qidu.jiajie.R;
import com.qidu.jiajie.databinding.ActivityLoginByMobileBinding;
import com.qidu.jiajie.mvp.contract.LoginContract;
import com.qidu.jiajie.mvp.presenter.LoginPresentImpl;


public class LoginByMobileFragment extends BaseFragment<LoginPresentImpl> implements LoginContract.View{
    private ActivityLoginByMobileBinding binding;

    private String loginRole;
    public static LoginByMobileFragment getInstance(String loginRole) {
        LoginByMobileFragment sf = new LoginByMobileFragment();
        sf.loginRole = loginRole;
        return sf;
    }
    @Override
    public void tokenByLoginResult(UserRealm user) {
        ToastUtils.show("登录成功");
        getActivity().finish();

    }

    @Override
    protected LoginPresentImpl initPresenter() {
        return new LoginPresentImpl(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.sendCode.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_login_by_mobile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(loginRole.equals(CommonKey.KEY_LOGIN_WORKER_OR_SELLER)){
            binding.loginTipLayout.setVisibility(View.VISIBLE);
            binding.loginByScanQr.setVisibility(View.VISIBLE);
        }
        binding.sendCode.setOnClickListener(v -> {
            //请求验证码
            String mobile=binding.mobileNum.getText().toString().trim();
            if(TextUtils.isEmpty(mobile)){
                ToastUtils.show("手机号码不能为空");
                return;
            }
            if(!RegexUtils.isMobileExact(mobile)){
                ToastUtils.show("手机号码错误");
                return;
            }
            mPresenter.getCode(mobile);
        });
        binding.btnLogin.setOnClickListener(v -> {
            String mobile=binding.mobileNum.getText().toString().trim();
            String password=binding.code.getText().toString().trim();
            if(TextUtils.isEmpty(mobile)){
                ToastUtils.show("手机号码不能为空");
                return;
            }
            if(TextUtils.isEmpty(password)){
                ToastUtils.show("请输入密码");
                return;
            }
            if(!RegexUtils.isMobileExact(mobile)){
                ToastUtils.show("手机号码错误");
                return;
            }
            mPresenter.getTokenByLogin(mobile,password);
        });
        binding.layoutTitle.headerLeftBtnImg.setImageDrawable(getResources().getDrawable(R.drawable.icon_back));
        binding.layoutTitle.headerLeftBtnImg.setOnClickListener(v -> {
            if(loginRole.equals(CommonKey.KEY_LOGIN_WORKER_OR_SELLER)){
                getActivity().finish();
            }else {
                getActivity().onBackPressed();
            }
        });
        binding.layoutTitle.headerText.setText("手机登录(免注册)");
    }

    @Override
    public void getCodeSuccess() {

    }

    @Override
    public void getCodeError() {

    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_by_mobile);


    }*/
}
