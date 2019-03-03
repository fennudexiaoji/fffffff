package com.common.lib.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T extends BaseRecyclerAdapter.ViewHolder, D> extends RecyclerView.Adapter<T> {
    protected List<D> mDataList = new ArrayList();
    private BaseRecyclerAdapter.OnItemClickListener mListener;
    private BaseRecyclerAdapter.OnItemLongClickListener mLongListener;

    public BaseRecyclerAdapter() {
    }

    public BaseRecyclerAdapter(@NonNull List<D> mDataList) {
        this.mDataList = mDataList;
    }

    @NonNull
    public List<D> getDataList() {
        return this.mDataList;
    }

    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        return this.createViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    public void onBindViewHolder(final T holder, int position) {
        if(this.isSafePosition(position)) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    boolean interrupt = BaseRecyclerAdapter.this.onItemClick(holder, holder.getBodyPosition());
                    if(!interrupt && BaseRecyclerAdapter.this.mListener != null) {
                        BaseRecyclerAdapter.this.mListener.onItemClick(v, holder.getBodyPosition(), BaseRecyclerAdapter.this.getItemId(holder.getBodyPosition()));
                    }

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    return BaseRecyclerAdapter.this.mLongListener != null?BaseRecyclerAdapter.this.mLongListener.onItemLongClick(v, holder.getBodyPosition(), BaseRecyclerAdapter.this.getItemId(holder.getBodyPosition())):false;
                }
            });
            this.onBindViewHolder(holder, position, this.getItemData(position));
        }
    }

    protected boolean onItemClick(T holder, int position) {
        return false;
    }

    public void setOnItemClickListener(@Nullable BaseRecyclerAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public void setOnItemLongClickListener(@Nullable BaseRecyclerAdapter.OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    public boolean isSafePosition(int position) {
        return position >= 0 && position < this.getItemCount();
    }

    public int getItemCount() {
        return this.mDataList.size();
    }

    public abstract T createViewHolder(LayoutInflater var1, ViewGroup var2, int var3);

    public abstract void onBindViewHolder(T var1, int var2, D var3);

    public D getItemData(int position) {
        return this.mDataList.get(position);
    }

    public void append(@Nullable List<D> list) {
        if(list != null && !list.isEmpty()) {
            int oldIndex = this.getItemCount();
            this.mDataList.addAll(list);
            this.notifyItemRangeInserted(oldIndex, list.size());
        }

    }

    public void refresh(@Nullable List<D> list) {
        this.mDataList.clear();
        if(list != null && !list.isEmpty()) {
            this.mDataList.addAll(list);
        }

        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        private RecyclerView parent;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public int getBodyPosition() {
            if(this.parent == null) {
                this.parent = (RecyclerView)this.itemView.getParent();
            }

            if(this.parent instanceof WrapRecyclerView) {
                WrapRecyclerView wrapRecyclerView = (WrapRecyclerView)this.parent;
                return this.getAdapterPosition() - wrapRecyclerView.getHeadersCount();
            } else {
                return this.getAdapterPosition();
            }
        }
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View var1, int var2, long var3);
    }

    public interface OnItemClickListener {
        void onItemClick(View var1, int var2, long var3);
    }
}
