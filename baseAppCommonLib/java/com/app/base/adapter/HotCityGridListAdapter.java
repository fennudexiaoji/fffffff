/*
package com.app.base.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.anthony.rvhelper.adapter.CommonAdapter;
import com.anthony.rvhelper.base.ViewHolder;
import com.app.base.R;
import com.app.base.bean.CityItem;
import com.app.base.bean.HotCity;
import com.app.base.listener.InnerListener;

import java.util.List;

*/
/**
 * Created by 7du-28 on 2018/4/19.
 *//*


public class HotCityGridListAdapter extends CommonAdapter<CityItem> {
    public static final int SPAN_COUNT = 3;

    private InnerListener mInnerListener;
    public HotCityGridListAdapter(Context context) {
        super(context, R.layout.cp_grid_item_layout);
    }


    @Override
    protected void convert(ViewHolder holder, final CityItem data, final int position) {
        */
/*holder.setText(R.id.tv_item_title, topic.getTitle());
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*//*

        if (data == null) return;
        View container=holder.getView(R.id.cp_grid_item_layout);
        //设置item宽高
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        */
/*TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.cpGridItemSpace, typedValue, true);
        int space = mContext.getResources().getDimensionPixelSize(typedValue.resourceId);*//*

        int space = mContext.getResources().getDimensionPixelSize(R.dimen.cp_grid_item_space);
        //int space=20;
        int padding = mContext.getResources().getDimensionPixelSize(R.dimen.cp_default_padding);
        int indexBarWidth = mContext.getResources().getDimensionPixelSize(R.dimen.cp_index_bar_width);
        int itemWidth = (screenWidth - padding - space * (SPAN_COUNT - 1) - indexBarWidth) / SPAN_COUNT;
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        lp.width = itemWidth;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        container.setLayoutParams(lp);
        holder.setText(R.id.cp_gird_item_name,data.getName());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInnerListener != null){
                    mInnerListener.dismiss(position, data);
                }
            }
        });
    }

    public void setInnerListener(InnerListener listener){
        this.mInnerListener = listener;
    }
}
*/
