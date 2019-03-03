package com.qidu.jiajie.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.bean.NeedServiceEntity;
import com.app.base.bean.OrderEntity;
import com.app.base.contract.GoodsContract;
import com.app.base.presenter.GoodsPresenterImpl;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.common.lib.base.BaseListPullRecyclerFragment;
import com.common.lib.databinding.LibFragmentBasePullrecyclerBinding;
import com.common.lib.divider.DividerItemDecoration;
import com.common.lib.global.AppGlobal;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.pullrecycler.layoutmanager.MyLinearLayoutManager;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.adapter.NeedListAdapter;
import com.qidu.jiajie.adapter.OrderListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

//订单-用户
public class OrderListFragment extends BaseListPullRecyclerFragment<GoodsPresenterImpl> implements
        GoodsContract.View{
    private LibFragmentBasePullrecyclerBinding binding;
    private String orderType="";
    private OrderListAdapter adapter;
    private GoodsPresenterImpl goodsPresenter;

    BroadcastReceiver broadcastReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(IntentParams.ACTION_REFRESH_ORDER_LIST)){
                refreshData();
            }
        }
    };
    public static OrderListFragment getInstance(String title) {
        OrderListFragment sf = new OrderListFragment();
        sf.orderType = title;
        return sf;
    }


    @Override
    protected GoodsPresenterImpl initPresenter() {
        goodsPresenter=new GoodsPresenterImpl();
        return goodsPresenter;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentParams.ACTION_REFRESH_ORDER_LIST);
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        MyLinearLayoutManager linearLayoutManager=new MyLinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration decoration=new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL,R.drawable.list_divider_ten_white);
        decoration.showLastFootViewDivider(false);
        binding.pullrecycler.getRecyclerView().setPadding(0,10,0,0);
        binding.pullrecycler.getRecyclerView().addItemDecoration(decoration);
        binding.pullrecycler.setLayoutManager(linearLayoutManager);
        adapter=new OrderListAdapter(getActivity());
        //binding.layoutRefresh.pullrecycler.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        //http://jiajie-touch-v2.wangyi120.cn/#/orderDetails?order_snx=15474346890000000000026
        adapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View var1, int position, long var3) {
                Intent intent=new Intent(getActivity(), WebAppActivity.class);
                intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+"/#/orderDetails?order_snx="+adapter.getDataList().get(position).getOrder_detail().getOrder_sn());
                getActivity().startActivity(intent);
            }
        });
        initPullRecycler(binding.statusViewLayout,binding.pullrecycler,adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(AppGlobal.isLogin()){
            //refreshData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.lib_fragment_base_pullrecycler, container, false);
        return binding.getRoot();
    }

    @Override
    public void loadData(int pageIndex) {
        JSONObject jsonObject=new JSONObject();
        if(orderType.equals("0")){//全部
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_type <>",3);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(orderType.equals("1")){//待付款
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_state",0);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(orderType.equals("2")){//待确认
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_state",1);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(orderType.equals("3")){//待服务
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_state",2);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(orderType.equals("4")){//服务中
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_state",3);
                conditionJson.put("order_comment_id",0);
                conditionJson.put("order_rate",1);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(orderType.equals("5")){//已关闭
            try {
                JSONObject conditionJson=new JSONObject();
                conditionJson.put("order_state",4);
                jsonObject.put("condition",conditionJson);
                jsonObject.put("page",/*pageIndex+*/pageIndex);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //String a="{\"page\":1,\"condition\":{\"order_type <>\":3}}";
        ////全部3 带付款0 待确认1 待服务2 服务中2 已关闭4
        goodsPresenter.getOrderList(jsonObject.toString());
    }

    @Override
    public void getNeedListResult(List<NeedServiceEntity> list) {

    }

    @Override
    public void getOrderListResult(List<OrderEntity> orderEntityList) {
        onDataSuccessReceived(pageNum,orderEntityList);
    }
}