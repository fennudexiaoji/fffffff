package com.qidu.jiajie.mvp.contract;

import com.app.base.bean.UserRealm;
import com.common.lib.base.view.IUIView;

public class LoginContract {
    public interface Presenter{
        void getTokenByLogin(String mobile,String verify_code);
        void getCode(String user_mobile);

        void loginByAliPay();

        void loginByWX(String wx_openid,String wx_nickname,String wx_unionid,String headimgurl);
    }


    public interface View extends IUIView {
        void tokenByLoginResult(UserRealm user);
        void getCodeSuccess();
        void getCodeError();
    }
}
