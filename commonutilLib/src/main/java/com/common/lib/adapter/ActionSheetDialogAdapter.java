package com.common.lib.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.lib.R;
import com.common.lib.bean.ActionItem;

import java.util.ArrayList;
import java.util.List;


public class ActionSheetDialogAdapter extends
        RecyclerView.Adapter<ActionSheetDialogAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<ActionItem> list = new ArrayList<>();
    private Context mContext;
    private boolean isShowSelectIcon;

    private boolean showTitle;

    private String itemTextColor;

    public ActionSheetDialogAdapter(Context context) {
        this.mContext=context;
        mInflater = LayoutInflater.from(context);
    }
    public void setItemTextColor(String itemTextColor){
        this.itemTextColor=itemTextColor;
    }
    public void showSelectIcon(boolean isShowSelectIcon){
        this.isShowSelectIcon=isShowSelectIcon;
    }
    public void setShowTitle(boolean showTitle){
        this.showTitle=showTitle;
    }
    public void setList(List<ActionItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public List<ActionItem> getDataList(){
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        ImageView selectIcon;
        View itemView;
        public ViewHolder(View view) {
            super(view);
            itemText= (TextView) view.findViewById(R.id.item_text);
            selectIcon= (ImageView) view.findViewById(R.id.select_status);
            itemView=view.findViewById(R.id.list_item);
        }
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_action_sheet_dialog_item,
                viewGroup, false);
        //view.getLayoutParams().height = ScreenUtils.dip2px(mContext,45);
        final ViewHolder viewHolder = new ViewHolder(view);
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(viewHolder.getAdapterPosition(), v);
                }
            });
        }
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        if(list.size()>0) {
            if(list.size()==1){
                if(showTitle){
                    viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                }else {
                    viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_single_selector);
                }
            }else {
                if (showTitle) {
                    if (position >= 0 && position < list.size()-1) {
                        viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else {
                        viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                } else {
                    if (position == 0) {
                        viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_top_selector);
                    } else if (position>0&&position < list.size()-1) {
                        viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
                    } else if(position>0&&position==list.size()-1){
                        viewHolder.itemView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
                    }
                }
            }
            ActionItem data = list.get(position);
            if (itemTextColor != null) {
                viewHolder.itemText.setTextColor(Color.parseColor(itemTextColor));
            }
            viewHolder.itemText.setText(data.getItemName());
            if (selectPosition != -1 && position == selectPosition&&isShowSelectIcon) {
                viewHolder.selectIcon.setVisibility(View.VISIBLE);
                //viewHolder.selectIcon.setBackgroundResource(R.drawable.icon_correct_select);
            } else {
                viewHolder.selectIcon.setVisibility(View.GONE);
                //viewHolder.selectIcon.setBackgroundResource(0);
            }
        }
    }
    private int selectPosition=-1;
    public void setSelectPosition(int position){
        this.selectPosition=position;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }



}
