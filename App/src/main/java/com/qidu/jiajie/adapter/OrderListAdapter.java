package com.qidu.jiajie.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.api.GoodsImplAPI;
import com.app.base.bean.OrderEntity;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.utils.ToastUtils;
import com.common.lib.widget.AppDialog;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.databinding.ListOrderListItemBinding;

import java.util.List;

import io.reactivex.functions.Consumer;

public class OrderListAdapter extends BaseRecyclerAdapter<OrderListAdapter.ViewHolder, OrderEntity> {
    private Context context;
    public OrderListAdapter(Context context){
        this.context=context;
    }
    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        View view = layoutInflater.inflate(R.layout.list_order_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, OrderEntity data) {
        viewHolder.onBind(position, data);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        ListOrderListItemBinding binding;
        ViewHolder(View view) {
            super(view);
            binding = ListOrderListItemBinding.bind(view);
        }
        void onBind(int position, OrderEntity data) {
            RequestOptions options =
                    new RequestOptions()
                            .centerCrop()
                            .dontAnimate();
            List<String> imgList=data.getOrder_detail().getOrder_img();
            String demand_img=imgList.size()>0?imgList.get(0):"";
            Glide.with(binding.orderImg)
                    .load(HttpUrl.API_HOST+"/"+demand_img)
                    .apply(options)
                    .into(binding.orderImg);
            binding.orderNum.setText("订单号："+data.getOrder_detail().getOrder_sn());
            binding.orderName.setText(data.getOrder_detail().getOrder_name());
            binding.orderTotal.setText("￥"+data.getPayment().getOrder_amount());
            binding.orderContent.setText(data.getOrder_detail().getOrder_info());
            String status=data.getOrder_detail().getOrder_state();
            if(status.equals("0")){
                //只显示取消订单按钮
                binding.orderCancel.setText("取消订单");
                binding.orderCancel.setVisibility(View.VISIBLE);
                binding.orderPay.setVisibility(View.VISIBLE);
                binding.waitComment.setVisibility(View.GONE);
                binding.orderCancel.setOnClickListener(v -> {
                    cancelOrder(position,data.getOrder_detail().getOrder_sn());
                });
                binding.orderPay.setOnClickListener(v -> {
                    String url="";
                    startWebActivity(url);
                });
                binding.orderStatus.setText("待付款");
            }else if(status.equals("1")){
                //只显示取消订单按钮
                binding.orderCancel.setText("取消订单");
                binding.orderCancel.setVisibility(View.VISIBLE);
                binding.orderPay.setVisibility(View.GONE);
                binding.waitComment.setVisibility(View.GONE);
                binding.orderCancel.setOnClickListener(v -> {
                    cancelOrder(position,data.getOrder_detail().getOrder_sn());
                });
                binding.orderStatus.setText("待分配");
            }else if(status.equals("2")){
                binding.orderStatus.setText("待上门");
                //只显示删除订单按钮
                binding.orderCancel.setText("取消订单");
                binding.orderCancel.setVisibility(View.VISIBLE);
                binding.orderPay.setVisibility(View.GONE);
                binding.waitComment.setVisibility(View.GONE);
                binding.orderCancel.setOnClickListener(v -> {
                    cancelOrder(position,data.getOrder_detail().getOrder_sn());
                });
            }else if(status.equals("3")){
                //只显示待评价
                binding.orderCancel.setVisibility(View.GONE);
                binding.orderPay.setVisibility(View.GONE);
                binding.waitComment.setVisibility(View.VISIBLE);
                binding.orderStatus.setText("待评价");
                binding.waitComment.setOnClickListener(v -> {
                    String url="";
                    startWebActivity(url);
                });
            }else if(status.equals("4")){
                binding.orderStatus.setText("已关闭");
                //只显示删除订单按钮
                binding.orderCancel.setText("删除订单");
                binding.orderCancel.setVisibility(View.VISIBLE);
                binding.orderPay.setVisibility(View.GONE);
                binding.waitComment.setVisibility(View.GONE);
                binding.orderCancel.setOnClickListener(v -> {
                    deleteOrder(position,data.getOrder_detail().getOrder_sn());
                });
            }else if(status.equals("5")){
                binding.orderStatus.setText("已完成");
                //只显示删除订单按钮
                binding.orderCancel.setText("删除订单");
                binding.orderCancel.setVisibility(View.VISIBLE);
                binding.orderPay.setVisibility(View.GONE);
                binding.waitComment.setVisibility(View.GONE);
                binding.orderCancel.setOnClickListener(v -> {
                    deleteOrder(position,data.getOrder_detail().getOrder_sn());
                });
            }
            binding.setOrder(data.getOrder_detail());
            binding.executePendingBindings();
        }


        /*@BindingAdapter({"bind:imageUrl"})
        static void loadImage(ImageView imageView, String imageUrl) {
            RequestOptions options =
                    new RequestOptions()
                            .centerCrop()
                            .dontAnimate();
            Glide.with(imageView)
                    .load(imageUrl)
                    .apply(options)
                    .into(imageView);
        }*/

    }
    public void startWebActivity(String url) {
        Intent intent=new Intent(context, WebAppActivity.class);
        intent.putExtra(IntentParams.WEB_URL, BuildConfig.DEFAULT_HOST+url);
        context.startActivity(intent);
    }


    private void cancelOrder(int position,String orderNum){
        new AppDialog.Builder(context).setCancel("取消").setOk("确定").setMsg(context.getResources().getString(R.string.cancel_order_tip)).setClickListener(new AppDialog.OnClickListener() {
            @Override
            public void onOkClick() {
                GoodsImplAPI.cancelOrder(orderNum).subscribe(new Consumer<BaseResponse<List>>() {
                    @Override
                    public void accept(BaseResponse<List> listBaseResponse) throws Exception {
                        ToastUtils.show("取消订单成功");
                        Intent intent=new Intent();
                        intent.setAction(IntentParams.ACTION_REFRESH_ORDER_LIST);
                        context.sendBroadcast(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(throwable.getMessage());
                    }
                });
            }
            @Override
            public void onCancelClick() {

            }
            @Override
            public void onDismiss() {

            }
        }).create();

    }

    private void deleteOrder(int position,String orderNum){
        new AppDialog.Builder(context).setCancel("取消").setOk("确定").setMsg("确定要删除订单吗").setClickListener(new AppDialog.OnClickListener() {
            @Override
            public void onOkClick() {
                GoodsImplAPI.deleteOrder(orderNum).subscribe(new Consumer<BaseResponse<List>>() {
                    @Override
                    public void accept(BaseResponse<List> listBaseResponse) throws Exception {
                        getDataList().remove(position);
                        notifyDataSetChanged();
                        ToastUtils.show("删除订单成功");
                        Intent intent=new Intent();
                        intent.setAction(IntentParams.ACTION_REFRESH_ORDER_LIST);
                        context.sendBroadcast(intent);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.show(throwable.getMessage());
                    }
                });
            }
            @Override
            public void onCancelClick() {

            }
            @Override
            public void onDismiss() {

            }
        }).create();
    }
}
