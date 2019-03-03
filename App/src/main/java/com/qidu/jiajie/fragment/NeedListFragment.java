package com.qidu.jiajie.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.bean.AppInfo;
import com.app.base.bean.NeedServiceEntity;
import com.app.base.bean.OrderEntity;
import com.app.base.contract.GoodsContract;
import com.app.base.presenter.GoodsPresenterImpl;

import com.app.base.utils.IntentParams;
import com.common.lib.base.BaseListPullRecyclerFragment;
import com.common.lib.databinding.LibFragmentBasePullrecyclerBinding;
import com.common.lib.divider.DividerItemDecoration;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.pullrecycler.layoutmanager.MyLinearLayoutManager;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.adapter.HomeAdapter;
import com.qidu.jiajie.adapter.NeedListAdapter;

import java.util.List;

//赚钱
public class NeedListFragment extends BaseListPullRecyclerFragment<GoodsPresenterImpl> implements
        GoodsContract.View{
    private LibFragmentBasePullrecyclerBinding binding;
    private String mTitle;
    private NeedListAdapter adapter;
    private GoodsPresenterImpl goodsPresenter;

    public static NeedListFragment getInstance(String title) {
        NeedListFragment sf = new NeedListFragment();
        sf.mTitle = title;
        return sf;
    }


    @Override
    protected GoodsPresenterImpl initPresenter() {
        goodsPresenter=new GoodsPresenterImpl();
        return goodsPresenter;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MyLinearLayoutManager linearLayoutManager=new MyLinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration=new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL,R.drawable.list_divider_ten_white);
        decoration.showLastFootViewDivider(false);
        binding.pullrecycler.getRecyclerView().setPadding(0,10,0,0);
        binding.pullrecycler.getRecyclerView().addItemDecoration(decoration);
        binding.pullrecycler.setLayoutManager(linearLayoutManager);
        adapter=new NeedListAdapter(getActivity());
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View var1, int position, long var3) {
                Intent intent=new Intent(getActivity(), WebAppActivity.class);
                intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+"/#/detailDem?item_id="+adapter.getItemData(position).getId());
                getActivity().startActivity(intent);
            }
        });
        //binding.layoutRefresh.pullrecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        initPullRecycler(binding.statusViewLayout,binding.pullrecycler,adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.lib_fragment_base_pullrecycler, container, false);
        return binding.getRoot();
    }

    @Override
    public void loadData(int pageIndex) {
        goodsPresenter.getNeedList(pageIndex);
    }

    @Override
    public void getNeedListResult(List<NeedServiceEntity> list) {
        onDataSuccessReceived(pageNum,list);
    }

    @Override
    public void getOrderListResult(List<OrderEntity> orderEntityList) {

    }
}