package com.common.lib.list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 *
 */

public class WrapRecyclerView extends RecyclerView {
    private WrapperAdapter mWrapAdapter;
    private WrapSpanSizeLookup lookup;
    private boolean shouldAdjustSpanSize;

    public WrapRecyclerView(Context context) {
        super(context);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAdapter(@NonNull Adapter adapter) {
        if(adapter instanceof WrapperAdapter) {
            this.mWrapAdapter = (WrapperAdapter)adapter;
            this.mWrapAdapter.setSpanSizeLookup(this.lookup);
            super.setAdapter(adapter);
        } else {
            this.mWrapAdapter = new WrapperAdapter(adapter);
            this.mWrapAdapter.setSpanSizeLookup(this.lookup);
            super.setAdapter(this.mWrapAdapter);
        }

        if(this.shouldAdjustSpanSize) {
            this.mWrapAdapter.adjustSpanSize(this);
        }

        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setSupportsChangeAnimations(false);
        this.setItemAnimator(itemAnimator);
    }

    public WrapperAdapter getAdapter() {
        return this.mWrapAdapter;
    }

    public Adapter getWrappedAdapter() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            return this.mWrapAdapter.getWrappedAdapter();
        }
    }

    public void setHeaderView(View view, int type) {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            this.mWrapAdapter.setHeaderView(view, type);
        }
    }

    public void removeHeaderView() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            this.mWrapAdapter.removeHeaderView();
        }
    }

    public void setFooterView(View view, int type) {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            this.mWrapAdapter.setFooterView(view, type);
        }
    }

    public void removeFooterView() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            this.mWrapAdapter.removeFooterView();
        }
    }

    public void setSpanSizeLookup(@Nullable WrapSpanSizeLookup lookup) {
        this.lookup = lookup;
    }

    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if(layout instanceof GridLayoutManager || layout instanceof StaggeredGridLayoutManager ) {
            this.shouldAdjustSpanSize = true;
        }

    }

    @Nullable
    public View getHeaderView() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            return this.mWrapAdapter.getHeaderView();
        }
    }

    @Nullable
    public View getFooterView() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            return this.mWrapAdapter.getFooterView();
        }
    }

    public int getHeadersCount() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            return this.mWrapAdapter.getHeaderCount();
        }
    }

    public int getFootersCount() {
        if(this.mWrapAdapter == null) {
            throw new IllegalStateException("You must set a adapter before!");
        } else {
            return this.mWrapAdapter.getFooterCount();
        }
    }
}
