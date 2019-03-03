package com.common.lib.adapter;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 7du-28 on 2017/9/26.
 */

public class AdapterHolder {
    private final SparseArray<View> mViews;
    private final int mPosition;
    private final View mConvertView;

    private AdapterHolder(ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray();
        this.mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        this.mConvertView.setTag(this);
    }

    public SparseArray<View> getAllView() {
        return this.mViews;
    }

    public static AdapterHolder get(View convertView, ViewGroup parent, int layoutId, int position) {
        return convertView == null?new AdapterHolder(parent, layoutId, position):(AdapterHolder)convertView.getTag();
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public <T extends View> T getView(int viewId) {
        View view = (View)this.mViews.get(viewId);
        if(view == null) {
            view = this.mConvertView.findViewById(viewId);
            this.mViews.put(viewId, view);
        }

        return (T) view;
    }

    public AdapterHolder setText(int viewId, String text) {
        TextView view = (TextView)this.getView(viewId);
        view.setText(text);
        return this;
    }

    public AdapterHolder setImageResource(int viewId, int drawableId) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

    public AdapterHolder setImageBitmap(int viewId, Bitmap bm) {
        ImageView view = (ImageView)this.getView(viewId);
        view.setImageBitmap(bm);
        return this;
    }

    /*public AdapterHolder setImageByUrl(KJBitmap bitmap, int viewId, String url) {
        bitmap.display(this.getView(viewId), url);
        return this;
    }*/

    public int getPosition() {
        return this.mPosition;
    }
}