package com.qidu.jiajie.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.bean.NeedServiceEntity;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.list.BaseRecyclerAdapter;
import com.qidu.jiajie.BuildConfig;
import com.qidu.jiajie.R;
import com.qidu.jiajie.activity.WebAppActivity;
import com.qidu.jiajie.databinding.ListNeedListItemBinding;

import java.util.List;

public class NeedListAdapter extends BaseRecyclerAdapter<NeedListAdapter.ViewHolder, NeedServiceEntity> {

    private Context context;
    public NeedListAdapter(Context context){
        this.context=context;
    }
    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        View view = layoutInflater.inflate(R.layout.list_need_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, NeedServiceEntity data) {
        viewHolder.onBind(position, data);
    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        ListNeedListItemBinding binding;
        ViewHolder(View view) {
            super(view);
            binding = ListNeedListItemBinding.bind(view);
        }
        void onBind(int position, NeedServiceEntity data) {
            RequestOptions options =
                    new RequestOptions()
                            .centerCrop()
                            .dontAnimate();
            List<String> imgList=data.getDemand_img();
            String demand_img=imgList.size()>0?imgList.get(0):"";
            Glide.with(binding.needImg)
                    .load(HttpUrl.API_HOST+"/"+demand_img)
                    .apply(options)
                    .into(binding.needImg);
            //http://jiajie-touch-v2.wangyi120.cn/#/detailDem?item_id=25
            binding.setNeed(data);
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
