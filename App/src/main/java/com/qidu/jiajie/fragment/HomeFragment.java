package com.qidu.jiajie.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.common.lib.base.BaseListPullRecyclerFragment;
import com.common.lib.divider.SpacesItemDecoration;
import com.common.lib.global.AppGlobal;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.pullrecycler.layoutmanager.MyStaggeredGridLayoutManager;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.SharedPreferencesUtils;
import com.liang.scancode.utils.Constant;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.LoginActivity;
import com.qidu.jiajie.activity.QRCodeScanActivity;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.adapter.GlideImageLoader;
import com.qidu.jiajie.adapter.HomeAdapter;
import com.qidu.jiajie.bean.ServiceEntity;
import com.qidu.jiajie.databinding.FragmentHomeBinding;
import com.qidu.jiajie.mvp.contract.HomeContract;
import com.qidu.jiajie.mvp.presenter.HomePresenterImpl;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class HomeFragment extends BaseListPullRecyclerFragment<HomePresenterImpl> implements HomeContract.View,View.OnClickListener{
    private FragmentHomeBinding binding;
    private HomeAdapter adapter;
    private HomePresenterImpl homePresenter;
    private View headerView;
    private Banner mXBanner;
    public static HomeFragment getInstance(String title) {
        HomeFragment sf = new HomeFragment();
        return sf;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== QRCodeScanActivity.QR_RESULT_CODE){
            if(data==null){
                return;
            }
            String result=data.getStringExtra(IntentParams.KEY_QR_CODE_SCAN_RESULT_VALUE);
            LogUtils.i("扫描返回："+result);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homePresenter.checkAppOnLineVersion(BuildConfig.api_version_check);
        binding.layoutTitle.headerRightBtnImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.icon_scan_qr));
        binding.layoutTitle.headerRightBtnImg.setOnClickListener(v -> {
            Intent intent=new Intent(getActivity(), QRCodeScanActivity.class);
            intent.putExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_QRCODE_MODE);
            startActivityForResult(intent,QRCodeScanActivity.QR_REQUEST_CODE);
        });
        headerView=getActivity().getLayoutInflater().inflate(R.layout.layout_home_header,null);
        View quickOrder=headerView.findViewById(R.id.quick_order);
        quickOrder.setOnClickListener(v -> {
            String url="/#/fastReservation";
            startWebActivity(url);
        });
        View publishNeed=headerView.findViewById(R.id.publish_need);
        publishNeed.setOnClickListener(v -> {
            if(AppGlobal.isLogin()){
                String url="/#/releaseDemand";
                startWebActivity(url);
            }else {
                Intent intent=new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        //获取控件
        mXBanner = (Banner) headerView.findViewById(R.id.banner);
        //设置banner样式
        mXBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        List<String> imageUrlList=new ArrayList<>();
        for(int i=0;i<3;i++) {
            imageUrlList.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547451890781&di=6a80d643d2dbfb9242fb1f237ef2aee1&imgtype=0&src=http%3A%2F%2Fpic29.nipic.com%2F20130531%2F8786105_102319220000_2.jpg");
        }
        mXBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mXBanner.setImages(imageUrlList);
        //设置banner动画效果
        //mXBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        //mXBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mXBanner.isAutoPlay(true);
        //设置轮播时间
        mXBanner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        mXBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mXBanner.start();
        binding.layoutTitle.headerLeftBtnImg.setVisibility(View.GONE);
        binding.layoutTitle.headerText.setText("首页");
        //binding.layoutRefresh.headerLeftBtnImg.
        MyStaggeredGridLayoutManager staggeredGridLayoutManager =
                new MyStaggeredGridLayoutManager(2,
                        MyStaggeredGridLayoutManager.VERTICAL);
        //解决item跳动
        staggeredGridLayoutManager.setGapStrategy(MyStaggeredGridLayoutManager.GAP_HANDLING_NONE);
        binding.layoutRefresh.pullrecycler.setLayoutManager(staggeredGridLayoutManager);
        adapter=new HomeAdapter(getActivity());
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View var1, int position, long var3) {
                String url="/#/service_list?id="+adapter.getItemData(position).getId();
                startWebActivity(url);
            }
        });
        /*SpacesItemDecoration decoration = new SpacesItemDecoration(20);
        binding.layoutRefresh.pullrecycler.addItemDecoration(decoration);*/
        initPullRecycler(binding.layoutRefresh.statusViewLayout,binding.layoutRefresh.pullrecycler,adapter);
        adapterWrapper.setHeaderView(headerView, -1024);
        refreshData();
    }
    public void startWebActivity(String url) {
        Intent intent=new Intent(getActivity(), WebAppActivity.class);
        intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+url);
        getActivity().startActivity(intent);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
        mXBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mXBanner.stopAutoPlay();
    }
    @Override
    protected HomePresenterImpl initPresenter() {
        homePresenter=new HomePresenterImpl(getActivity());
        return homePresenter;
    }

    @Override
    public void loadData(int pageIndex) {
        homePresenter.loadServiceList(pageIndex,"");
    }


    @Override
    public void getServiceList(List<ServiceEntity> serviceEntityList) {
        onDataSuccessReceived(pageNum,serviceEntityList);
    }
}