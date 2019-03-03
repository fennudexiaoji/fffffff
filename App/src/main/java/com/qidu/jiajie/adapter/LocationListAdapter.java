package com.qidu.jiajie.adapter;


import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.common.lib.adapter.AdapterHolder;
import com.common.lib.adapter.BaseListAndGridAdapter;
import com.qidu.jiajie.R;

import java.util.Collection;


public class LocationListAdapter extends BaseListAndGridAdapter<PoiItem> {
    private int selectPosition=-1;
    public LocationListAdapter(AbsListView view, Collection<PoiItem> mDatas) {
        super(view, mDatas, R.layout.layout_adapter_gaodemap_item);
    }

    @Override
    public void convert(AdapterHolder holder, int position, boolean isScrolling) {
        PoiItem itemData = (PoiItem)getItem(position);
        //boolean isSelected = (boolean) data.get("selected");
        TextView name = holder.getView(R.id.adapter_baidumap_location_name);
        TextView address = holder.getView(R.id.adapter_baidumap_location_address);
        ImageView checked = holder.getView(R.id.adapter_baidumap_location_checked);
        if (position == selectPosition) {
            checked.setVisibility(View.VISIBLE);
        } else {
            checked.setVisibility(View.GONE);
        }
        name.setText(itemData.getTitle());
        address.setText(itemData.getSnippet());
    }
    public void setSelection(int selectPosition) {
        this.selectPosition = selectPosition;
        this.notifyDataSetChanged();
    }

    public int getSelection(){
        return selectPosition;
    }
}

