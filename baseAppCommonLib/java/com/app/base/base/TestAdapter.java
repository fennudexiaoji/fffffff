package com.app.base.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.base.R;
import com.app.base.bean.Store;
import com.common.lib.list.BaseRecyclerAdapter;
import com.common.lib.list.BaseRecyclerAdapter.ViewHolder;

/**
 * Created by 7du-28 on 2018/7/28.
 */

public class TestAdapter extends BaseRecyclerAdapter<ViewHolder, String>{
    @Override
    public BaseRecyclerAdapter.ViewHolder createViewHolder(LayoutInflater layoutInflater, ViewGroup viewGroup, int type) {
        View view = layoutInflater.inflate(R.layout.layout_common_title, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerAdapter.ViewHolder viewHolder, int position, String data) {

    }

    class ViewHolder extends BaseRecyclerAdapter.ViewHolder {
        //ListExpressBinding binding;
        ViewHolder(View view) {
            super(view);
            //binding = ListExpressBinding.bind(view);
        }
        /*void onBind(int position, ExpressItem data) {
            binding.setExpress(data);
            binding.executePendingBindings();
        }*/
    }
}
