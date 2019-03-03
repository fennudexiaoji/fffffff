package com.qidu.jiajie.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.api.GoodsImplAPI;
import com.app.base.bean.OrderStatusBean;
import com.app.base.bean.StaffUser;
import com.app.base.bean.StoreOwnBean;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.base.AbsBaseFragment;
import com.common.lib.global.AppGlobal;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.LoginActivity;
import com.qidu.jiajie.activity.SettingActivity;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.databinding.FragmentWorkHomeBinding;
import com.qidu.jiajie.mvp.model.LoginImplAPI;

import io.reactivex.functions.Consumer;

/**
 * 帮家洁工作台首页
 * 清洁师账号
 * 13660237210
 * 123456
 */

public class WorkHomeFragment extends AbsBaseFragment{
    private FragmentWorkHomeBinding binding;
    private StaffUser user;
    private StoreOwnBean storeOwn;
    private String mTitle;
    public static WorkHomeFragment getInstance(String title) {
        WorkHomeFragment sf = new WorkHomeFragment();
        sf.mTitle = title;
        return sf;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //fragment_work_home
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.title_parent_top).setAlpha(1.0f);
        binding.layoutTitle.headerLeftBtnImg.setVisibility(View.GONE);
        binding.layoutTitle.headerText.setText("我的");
        binding.userCode.setOnClickListener(v -> {
            if(storeOwn!=null){
                ScanCodeQRShowFragment scanCodeQRShowFragment=ScanCodeQRShowFragment.getInstance("扫一扫，加入店铺",storeOwn.getOwn_store().getStore_inducted_qrcode());
                FragmentTransaction ft = getChildFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                scanCodeQRShowFragment.show(getActivity().getFragmentManager(),"scan_qr");
            }
        });
        binding.manageAchievements.setOnClickListener(v -> {
            if(user!=null){
                String url="/#/store_orders_x?staff_id=61"+user.getId();
                startWebActivity(url);
            }
        });

        binding.manageComment.setOnClickListener(v -> {
            String url="/#/commentAdmin";
            startWebActivity(url);
        });

        binding.lookDetail.setOnClickListener(v -> {
            String url="/#/storeProfit";
            startWebActivity(url);

        });
        binding.cashNow.setOnClickListener(v -> {
            String url="/#/balance_cash?cashNum=1&value=0.00&way_type=store";
            startWebActivity(url);
        });
        binding.settingClick.setOnClickListener(v -> {
            if(AppGlobal.isLogin()) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppGlobal.isLogin()){
            requestStoreInfo();
        }
    }

    private void requestStoreInfo(){
        if(AppGlobal.isLogin()) {
            LoginImplAPI.getStoreInfo().subscribe(new Consumer<StoreOwnBean>() {
                @Override
                public void accept(StoreOwnBean storeOwnBean) throws Exception {
                    binding.refreshLayout.setRefreshing(false);
                    if (storeOwnBean != null) {
                        initUser(storeOwnBean);
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
                    binding.refreshLayout.setRefreshing(false);
                    initOrderStatus(orderStatusBean);

                }
            });

            LoginImplAPI.getStaffInfo().subscribe(new Consumer<StaffUser>() {
                @Override
                public void accept(StaffUser staffUser) throws Exception {
                    binding.refreshLayout.setRefreshing(false);
                    if (staffUser != null) {
                        user=staffUser;
                        binding.setStaff(staffUser);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    binding.refreshLayout.setRefreshing(false);
                }
            });

        }
    }

    private void initUser(StoreOwnBean storeOwnBean){
        storeOwn=storeOwnBean;
        binding.setStore(storeOwnBean.getOwn_store());
        RequestOptions options =
                new RequestOptions()
                        .circleCrop()
                        .dontAnimate();
        if(storeOwnBean.getOwn_store().getStore_pic().size()>0){
            String storeUrlPic=storeOwnBean.getOwn_store().getStore_pic().get(0);
            Glide.with(getActivity())
                    .load(HttpUrl.API_HOST+"/"+storeUrlPic)
                    .apply(options)
                    .into(binding.userIcon);
        }
        /*Glide.with(getActivity())
                .load(HttpUrl.API_HOST+"/"+storeOwnBean.getOwn_store().getStore_inducted_qrcode())
                .into(binding.userCode);*/

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
