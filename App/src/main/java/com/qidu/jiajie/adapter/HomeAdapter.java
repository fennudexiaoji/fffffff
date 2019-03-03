package com.qidu.jiajie.adapter;


import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.utils.ScreenUtils;
import com.common.lib.utils.ToastUtils;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.bean.ServiceEntity;
import com.qidu.jiajie.databinding.ListServiceTypeItemBinding;

public class HomeAdapter extends BaseRecyclerAdapter<HomeAdapter.ViewHolder, ServiceEntity> {

    private Context context;
    public HomeAdapter(Context context){
        this.context=context;
    }
    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        View view = layoutInflater.inflate(R.layout.list_service_type_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, ServiceEntity data) {
        viewHolder.onBind(position, data);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        ListServiceTypeItemBinding binding;
        ViewHolder(View view) {
            super(view);
            binding = ListServiceTypeItemBinding.bind(view);
        }
        void onBind(int position, ServiceEntity data) {
            //float imageWidth= (ScreenUtils.getScreenWidth(context)-5*3-40)/2;
            RequestOptions options =
                    new RequestOptions()
                            .fitCenter()
                            .dontAnimate();
            Glide.with(binding.serviceImg)
                    .load(HttpUrl.API_HOST+"/"+data.getCate_cover())
                    .apply(options)
                    .into(binding.serviceImg);
            //http://jiajie-touch-v2.wangyi120.cn/#/service_list
            binding.setService(data);
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
}
