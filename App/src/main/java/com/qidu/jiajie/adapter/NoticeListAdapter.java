package com.qidu.jiajie.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.base.bean.NeedServiceEntity;
import com.app.base.utils.CommonKey;
import com.app.base.utils.HttpUrl;
import com.app.base.utils.IntentParams;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.common.lib.list.BaseRecyclerAdapter;
import com.flyco.tablayout.utils.UnreadMsgUtils;
import com.flyco.tablayout.widget.MsgView;
import com.qidu.jiajie.R;
import com.qidu.jiajie.bean.Notice;
import com.qidu.jiajie.databinding.LayoutNoticeListItemBinding;
import com.qidu.jiajie.databinding.ListNeedListItemBinding;

import java.util.List;

/**
 * 项目列表
 */

public class NoticeListAdapter extends BaseRecyclerAdapter<NoticeListAdapter.ViewHolder, Notice> {
    private Context context;
    public NoticeListAdapter(Context context){
        this.context=context;
    }

    @Override
    public ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        View view = layoutInflater.inflate(R.layout.layout_notice_list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position, Notice data) {
        viewHolder.onBind(position, data);
    }
    /*@Override
    protected void convert(ViewHolder holder, final Notice data, int position) {
        MsgView msgView=holder.getView(R.id.rtv_msg_tip);
        msgView.setVisibility(View.GONE);
        if(data.getUnReadCount()!=null){
            msgView.setVisibility(View.VISIBLE);
            msgView.setText(data.getUnReadCount());
        }
        TextView content=holder.getView(R.id.content);
        content.setText(data.getBulletin_content());
        ImageView img_notice=holder.getView(R.id.img_notice);
        if(data.getMsgType().equals(CommonKey.KEY_NOTICE_SYSTEM)){
            img_notice.setImageResource(R.drawable.icon_system_notice);
        }else if(data.getMsgType().equals(CommonKey.KEY_NOTICE_PUBLIC)){
            img_notice.setImageResource(R.drawable.icon_announcement_notice);
        }else if(data.getMsgType().equals(CommonKey.KEY_NOTICE_APPROVAL)){
            img_notice.setImageResource(R.drawable.icon_approval);
        }
        holder.setText(R.id.title, data.getTitle());

    }*/

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        LayoutNoticeListItemBinding binding;
        ViewHolder(View view) {
            super(view);
            binding = LayoutNoticeListItemBinding.bind(view);
        }
        void onBind(int position, Notice data) {
            RequestOptions options =
                    new RequestOptions()
                            .centerCrop()
                            .dontAnimate();

            /*Glide.with(binding.needImg)
                    .load(HttpUrl.API_HOST+"/"+demand_img)
                    .apply(options)
                    .into(binding.needImg);*/
            if(data.getMsgType().equals(CommonKey.KEY_NOTICE_SYSTEM)){
                binding.imgNotice.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_sys_notice));
            }else if(data.getMsgType().equals(CommonKey.KEY_NOTICE_SYSTEM)){
                binding.imgNotice.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_jiajia_comsumer));
            }
            if(data.getUnread()!=null){
                if(!data.getUnread().equals("0")){
                    UnreadMsgUtils.show(binding.unreadCount, Integer.parseInt(data.getUnread()));
                }
            }
            binding.setNotice(data);
            binding.executePendingBindings();
        }

    }
}
