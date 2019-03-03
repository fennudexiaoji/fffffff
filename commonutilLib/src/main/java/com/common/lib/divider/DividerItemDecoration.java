package com.common.lib.divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 7du-28 on 2017/5/27.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    private Drawable mDivider;
    private int mOrientation;
    private boolean showLastFootViewDivider=true;
    public void showLastFootViewDivider(boolean showLastFootViewDivider){
        this.showLastFootViewDivider=showLastFootViewDivider;
    }
    public DividerItemDecoration(Context context, int orientation) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        this.mDivider = a.getDrawable(0);
        a.recycle();
        this.setOrientation(orientation);
    }
    /**
     * 自定义分割线
     *
     * @param context
     * @param orientation 列表方向
     * @param drawableId  分割线图片
     */
    public DividerItemDecoration(Context context, int orientation, int drawableId) {
        this(context, orientation);
        mDivider = ContextCompat.getDrawable(context, drawableId);
    }
    public void setOrientation(int orientation) {
        if(orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("invalid orientation");
        } else {
            this.mOrientation = orientation;
        }
    }
    private int footViewCount=0;
    public void setFootViewCount(int footViewCount){//有上啦加载的时候使用
        this.footViewCount=footViewCount;
    }


    private int headerViewCount=0;
    public void setHeaderViewCount(int headerViewCount){
        this.headerViewCount=headerViewCount;
    }
    public void onDraw(Canvas c, RecyclerView parent) {
        if(this.mOrientation == 1) {
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount;
        if(this.footViewCount>0){//1为footView个数
            childCount = parent.getChildCount()-this.footViewCount;
        }else {
            childCount = parent.getChildCount();
        }
        if(!showLastFootViewDivider){
            childCount=childCount-1;
        }
        if(this.headerViewCount>0){
            for(int i = this.headerViewCount; i < childCount; ++i) {//实际添加header的时候i要改成大于header个数
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + this.mDivider.getIntrinsicHeight();
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(c);
            }
        }else {
            for(int i = 0; i < childCount; ++i) {//实际添加header的时候i要改成大于header个数
                View child = parent.getChildAt(i);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + this.mDivider.getIntrinsicHeight();
                this.mDivider.setBounds(left, top, right, bottom);
                this.mDivider.draw(c);
            }
        }


    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();

        for(int i = 0; i < childCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            int left = child.getRight() + params.rightMargin;
            int right = left + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(c);
        }

    }

    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if(this.mOrientation == 1) {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
        }

    }
}