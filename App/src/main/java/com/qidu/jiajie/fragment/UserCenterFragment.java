package com.qidu.jiajie.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.api.GoodsImplAPI;
import com.app.base.bean.OrderEntity;
import com.app.base.bean.OrderStatusBean;
import com.app.base.bean.UserRealm;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.base.AbsBaseFragment;
import com.common.lib.global.AppGlobal;
import com.common.lib.utils.SharedPreferencesUtils;
import com.common.lib.utils.ToastUtils;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.LoginActivity;
import com.qidu.jiajie.activity.SettingActivity;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.databinding.FragmentUserCenterBinding;
import com.qidu.jiajie.mvp.model.LoginImplAPI;

import io.reactivex.functions.Consumer;
import io.realm.Realm;

/**
 *会员中心
 */

public class UserCenterFragment extends AbsBaseFragment{
    private FragmentUserCenterBinding binding;
    private String mTitle;
    private UserRealm user;
    public static UserCenterFragment getInstance(String title) {
        UserCenterFragment sf = new UserCenterFragment();
        sf.mTitle = title;
        return sf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_center, container, false);
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        requestUserInfo();
    }

    private void requestUserInfo(){
        if(AppGlobal.isLogin()) {
            LoginImplAPI.getUserInfo().subscribe(new Consumer<UserRealm>() {
                @Override
                public void accept(UserRealm userRealm) throws Exception {
                    binding.refreshLayout.setRefreshing(false);
                    if (userRealm != null) {
                        UserRealm.insertUserRealm(userRealm, new Realm.Transaction.OnSuccess() {
                            @Override
                            public void onSuccess() {
                                initUser(userRealm);
                            }
                        }, new Realm.Transaction.OnError() {
                            @Override
                            public void onError(Throwable error) {
                            }
                        });
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    binding.refreshLayout.setRefreshing(false);
                }
            });
            GoodsImplAPI.getUserOrderStatus().subscribe(new Consumer<OrderStatusBean>() {
                @Override
                public void accept(OrderStatusBean orderStatusBean) throws Exception {
                    initOrderStatus(orderStatusBean);
                }
            });
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.layoutTitle.headerLeftBtnImg.setVisibility(View.GONE);
        binding.layoutTitle.headerText.setText("个人中心");
        binding.refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestUserInfo();
            }
        });
        binding.codeImg.setOnClickListener(v -> {
            if(user!=null){
                ScanCodeQRShowFragment scanCodeQRShowFragment=ScanCodeQRShowFragment.getInstance("下单出示会员码，可累积积分",user.getUser_qrcode());
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                scanCodeQRShowFragment.show(getActivity().getFragmentManager(),"scan_qr");
            }
        });
        String userId= (String) SharedPreferencesUtils.getInstance(getActivity()).getData(AppGlobal.KEY_LOGIN_USER_ID,"");
        if(!TextUtils.isEmpty(userId)){
            UserRealm userRealm=UserRealm.queryUserRealmById(userId);
            initUser(userRealm);
            binding.userPic.setOnClickListener(v -> {
                String url="/#/pers";
                startWebActivity(url);
            });
            binding.moneyTotal.setOnClickListener(v -> {
                String url="/#/balance";
                startWebActivity(url);
            });

            binding.scoreTotal.setOnClickListener(v -> {
                String url="/#/credit";
                startWebActivity(url);
            });
            binding.favouriteTotal.setOnClickListener(v -> {
                String url="/#/myColl";
                startWebActivity(url);
            });
        }

        binding.publishClick.setOnClickListener(v -> {
            String url="/#/releaseDemand";
            startWebActivity(url);
        });
        binding.inviteClick.setOnClickListener(v -> {
            String url="/#/inviting?user_id="+AppGlobal.getUserId();
            startWebActivity(url);
        });
        binding.commentClick.setOnClickListener(v -> {
            String url="/#/myeval";
            startWebActivity(url);
        });
        binding.addressClick.setOnClickListener(v -> {
            String url="/#/receadd";
            startWebActivity(url);
        });
        binding.customClick.setOnClickListener(v -> {
            String url="/#/service";
            startWebActivity(url);
        });
        binding.aboutClick.setOnClickListener(v -> {
            String url="/#/about";
            startWebActivity(url);
        });
        binding.settingClick.setOnClickListener(v -> {
            if(AppGlobal.isLogin()) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });

    }

    private void initUser(UserRealm userRealm){
        user=userRealm;
        binding.setUser(userRealm);
        RequestOptions options =
                new RequestOptions()
                        .circleCrop()
                        .dontAnimate();
        Glide.with(getActivity())
                .load(HttpUrl.API_HOST+"/"+userRealm.getUser_pic())
                .apply(options)
                .into(binding.userPic);
        /*Glide.with(getActivity())
                .load(HttpUrl.API_HOST+"/"+userRealm.getUser_qrcode())
                .into(binding.codeImg);*/

    }

    private void initOrderStatus(OrderStatusBean orderStatusBean){
        binding.needPay.setText(orderStatusBean.getPending_pay()+"");
        binding.needPay.setOnClickListener(v -> {
            String url="/#/orders?index=1";
            startWebActivity(url);
        });
        binding.waitOrder.setText(orderStatusBean.getPending_service()+"");
        binding.waitOrder.setOnClickListener(v -> {
            String url="/#/orders?index=2";
            startWebActivity(url);
        });
        binding.waitService.setText(orderStatusBean.getPending_service()+"");
        binding.waitService.setOnClickListener(v -> {
            String url="/#/orders?index=3";
            startWebActivity(url);
        });
        binding.inService.setText(orderStatusBean.getIn_service()+"");
        binding.inService.setOnClickListener(v -> {
            String url="/#/orders?index=3";
            startWebActivity(url);
        });
        binding.waitService.setText(orderStatusBean.getPending_service()+"");
        binding.waitService.setOnClickListener(v -> {
            String url="/#/orders?index=3";
            startWebActivity(url);
        });
        binding.pendingAssign.setText(orderStatusBean.getPending_assign()+"");
        binding.pendingAssign.setOnClickListener(v -> {
            String url="/#/orders?index=3";
            startWebActivity(url);
        });
        binding.waitComment.setText(orderStatusBean.getPending_comment()+"");
        binding.waitComment.setOnClickListener(v -> {
            String url="/#/orders?index=4";
            startWebActivity(url);
        });
        binding.orderCancel.setText(orderStatusBean.getClosed()+"");
        binding.orderCancel.setOnClickListener(v -> {
            String url="/#/orders?index=5";
            startWebActivity(url);
        });
    }
    public void startWebActivity(String url) {
        if(AppGlobal.isLogin()){
            Intent intent=new Intent(getActivity(), WebAppActivity.class);
            intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+url);
            getActivity().startActivity(intent);
        }else {
            Intent intent=new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }
    }

}
