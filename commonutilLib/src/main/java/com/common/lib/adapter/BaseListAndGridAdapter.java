package com.common.lib.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created by 7du-28 on 2017/9/26.
 */

public abstract class BaseListAndGridAdapter<T> extends BaseAdapter implements AbsListView.OnScrollListener {
    protected LayoutInflater mInflater;
    protected Collection<T> mDatas;
    protected final int mItemLayoutId;
    protected AbsListView mList;
    protected boolean isScrolling;
    private AbsListView.OnScrollListener listener;

    public BaseListAndGridAdapter(AbsListView view, Collection<T> mDatas, int itemLayoutId) {
        this.mInflater = LayoutInflater.from(view.getContext());
        if(mDatas == null) {
            mDatas = new ArrayList(0);
        }

        this.mDatas = (Collection)mDatas;
        this.mItemLayoutId = itemLayoutId;
        this.mList = view;
        this.mList.setOnScrollListener(this);
    }

    public void refresh(Collection<T> datas) {
        if(datas == null) {
            datas = new ArrayList(0);
        }
        this.mDatas.clear();
        this.mDatas = (Collection)datas;
        this.notifyDataSetChanged();
    }

    public void append(Collection<T> datas){
        this.mDatas.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void addOnScrollListener(AbsListView.OnScrollListener l) {
        this.listener = l;
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public T getItem(int position) {
        return (T) (this.mDatas instanceof List ?((List)this.mDatas).get(position):(this.mDatas instanceof Set ?(new ArrayList(this.mDatas)).get(position):null));
    }

    public long getItemId(int position) {
        return (long)position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder viewHolder = this.getViewHolder(position, convertView, parent);
        this.convert(viewHolder, position, this.isScrolling);
        return viewHolder.getConvertView();
    }

    private AdapterHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return AdapterHolder.get(convertView, parent, this.mItemLayoutId, position);
    }

    public abstract void convert(AdapterHolder var1, int position, boolean var3);

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState == 0) {
            this.isScrolling = false;
            this.notifyDataSetChanged();
        } else {
            this.isScrolling = true;
        }

        if(this.listener != null) {
            this.listener.onScrollStateChanged(view, scrollState);
        }

    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(this.listener != null) {
            this.listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

    }
}
