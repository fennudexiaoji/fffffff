package com.common.lib.list;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.common.lib.utils.LogUtils;

public class WrapperAdapter<T extends RecyclerView.Adapter> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String TAG = WrapperAdapter.class.getSimpleName();
    private final T mRealAdapter;
    private boolean isStaggeredGrid;
    private WrapSpanSizeLookup lookup;
    private static final int BASE_HEADER_VIEW_TYPE = -1024;
    private static final int BASE_FOOTER_VIEW_TYPE = -2048;
    private WrapperAdapter<T>.WrapInfo headerView;
    private WrapperAdapter<T>.WrapInfo footerView;
    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        public void onChanged() {
            LogUtils.i(WrapperAdapter.this.TAG, "onChanged");
            WrapperAdapter.this.notifyDataSetChanged();
        }

        public void onItemRangeChanged(int positionStart, int itemCount) {
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeChanged " + positionStart + "," + itemCount);
            int adapterPosition = positionStart + WrapperAdapter.this.getHeaderCount();
            WrapperAdapter.this.notifyItemRangeChanged(adapterPosition, itemCount);
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeChanged adapter " + adapterPosition + "," + itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeInserted " + positionStart + "," + itemCount);
            int adapterPosition = positionStart + WrapperAdapter.this.getHeaderCount();
            WrapperAdapter.this.notifyItemRangeInserted(adapterPosition, itemCount);
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeInserted adapter " + adapterPosition + "," + itemCount);
        }

        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeMoved " + fromPosition + "," + toPosition + "," + itemCount);
            int adapterFromPosition = fromPosition + WrapperAdapter.this.getHeaderCount();
            int adapterToPosition = toPosition + WrapperAdapter.this.getHeaderCount();
            WrapperAdapter.this.notifyItemMoved(adapterFromPosition, adapterToPosition);
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeMoved adapter " + adapterFromPosition + "," + adapterToPosition);
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeRemoved " + positionStart + "," + itemCount);
            int adapterPosition = positionStart + WrapperAdapter.this.getHeaderCount();
            WrapperAdapter.this.notifyItemRangeRemoved(adapterPosition, itemCount);
            LogUtils.i(WrapperAdapter.this.TAG, "onItemRangeRemoved adapter " + adapterPosition + "," + itemCount);
        }
    };

    public WrapperAdapter(@NonNull T adapter) {
        this.mRealAdapter = adapter;
    }

    public T getWrappedAdapter() {
        return this.mRealAdapter;
    }

    public void setHeaderView(View view, int type) {
        if(null == view) {
            throw new IllegalArgumentException("the view to add must not be null!");
        } else {
            boolean insert = this.headerView == null;
            this.headerView = new WrapperAdapter.WrapInfo(view, type + -1024);
            if(insert) {
                this.notifyItemInserted(0);
            } else {
                this.notifyItemChanged(0);
            }

        }
    }

    public void removeHeaderView() {
        Log.i(this.TAG, "removeHeaderView");
        this.headerView = null;
        this.notifyItemRemoved(0);
    }

    public void setFooterView(View view, int type) {
        Log.i(this.TAG, "addFooterView");
        if(null == view) {
            throw new IllegalArgumentException("the view to add must not be null!");
        } else {
            boolean insert = this.footerView == null;
            this.footerView = new WrapperAdapter.WrapInfo(view, type + -2048);
            if(insert) {
                this.notifyItemInserted(this.getItemCount() - 1);
            } else {
                this.notifyItemChanged(this.getItemCount() - 1);
            }

        }
    }

    public void removeFooterView() {
        Log.i(this.TAG, "removeFooterView");
        this.footerView = null;
        this.notifyItemRemoved(this.getItemCount());
    }

    @Nullable
    public View getHeaderView() {
        return this.headerView != null?this.headerView.view:null;
    }

    @Nullable
    public View getFooterView() {
        return this.footerView != null?this.footerView.view:null;
    }

    public void setSpanSizeLookup(@Nullable WrapSpanSizeLookup lookup) {
        this.lookup = lookup;
    }

    public void adjustSpanSize(@NonNull RecyclerView recycler) {
        if(recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager)recycler.getLayoutManager();
            final GridLayoutManager.SpanSizeLookup sizeLookup = layoutManager.getSpanSizeLookup();
            if(this.lookup != null) {
                this.lookup.setSpanCount(layoutManager.getSpanCount());
            }

            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = WrapperAdapter.this.isHeaderPosition(position) || WrapperAdapter.this.isFooterPosition(position);
                    int bodyPosition = position - WrapperAdapter.this.getHeaderCount();
                    return isHeaderOrFooter?layoutManager.getSpanCount():(WrapperAdapter.this.lookup == null?sizeLookup.getSpanSize(bodyPosition):WrapperAdapter.this.lookup.getSpanSize(bodyPosition));
                }
            });
        }

        if(recycler.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            this.isStaggeredGrid = true;
        }

    }

    public int getHeaderCount() {
        return this.headerView == null?0:1;
    }

    public int getFooterCount() {
        return this.footerView == null?0:1;
    }

    private boolean isHeaderPosition(int position) {
        return position < this.getHeaderCount();
    }

    private boolean isFooterPosition(int position) {
        return position >= this.getHeaderCount() + this.mRealAdapter.getItemCount();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return this.headerView != null && this.headerView.type == viewType?this.createHeaderFooterViewHolder(this.headerView.view):(this.footerView != null && this.footerView.type == viewType?this.createHeaderFooterViewHolder(this.footerView.view):this.mRealAdapter.onCreateViewHolder(viewGroup, viewType));
    }

    private RecyclerView.ViewHolder createHeaderFooterViewHolder(View view) {
        if(this.isStaggeredGrid) {
            StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(-1, -2);
            params.setFullSpan(true);
            view.setLayoutParams(params);
        }

        return new WrapperAdapter.WarpViewHolder(view);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(position >= this.getHeaderCount() && position < this.getHeaderCount() + this.mRealAdapter.getItemCount()) {
            this.mRealAdapter.onBindViewHolder(viewHolder, position - this.getHeaderCount());
        }

    }

    public int getItemCount() {
        return this.getHeaderCount() + this.mRealAdapter.getItemCount() + this.getFooterCount();
    }

    public int getItemViewType(int position) {
        return this.isHeaderPosition(position)?this.headerView.type:(this.isFooterPosition(position)?this.footerView.type:this.mRealAdapter.getItemViewType(position - this.getHeaderCount()));
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        this.mRealAdapter.unregisterAdapterDataObserver(this.adapterDataObserver);
    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        this.mRealAdapter.registerAdapterDataObserver(this.adapterDataObserver);
    }

    private static class WarpViewHolder extends RecyclerView.ViewHolder {
        WarpViewHolder(View view) {
            super(view);
        }
    }

    private class WrapInfo {
        int type;
        View view;

        WrapInfo(View view, int type) {
            this.type = type;
            this.view = view;
        }
    }
}
