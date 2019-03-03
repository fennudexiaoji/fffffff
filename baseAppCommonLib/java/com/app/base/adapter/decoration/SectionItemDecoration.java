/*
package com.app.base.adapter.decoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import com.app.base.R;
import com.app.base.bean.CityItem;

import java.util.List;

public class SectionItemDecoration extends RecyclerView.ItemDecoration {
    private List<CityItem> mData;
    private Paint mBgPaint;
    private TextPaint mTextPaint;
    private Rect mBounds;

    private int mSectionHeight;
    private int mBgColor;
    private int mTextColor;
    private int mTextSize;

    public SectionItemDecoration(Context context, List<CityItem> data) {
        this.mData = data;

        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.SectionItemDecoration);
        mTextSize =typedArray.getDimensionPixelSize(R.styleable.SectionItemDecoration_cpSectionTextSize,30);//给他赋值一个红色
        mBgColor =typedArray.getColor(R.styleable.SectionItemDecoration_cpSectionBackground,0xCCCCCCCC);//给他赋值一个红色
        mTextColor = typedArray.getColor(R.styleable.SectionItemDecoration_cpSectionTextColor,0xffff0000);
        mSectionHeight=typedArray.getDimensionPixelSize(R.styleable.SectionItemDecoration_cpSectionHeight,60);
        typedArray.recycle();




        */
/*TypedValue typedValue = new TypedValue();

        context.getTheme().resolveAttribute(R.attr.cpSectionBackground, typedValue, true);
        mBgColor = context.getResources().getColor(typedValue.resourceId);

        context.getTheme().resolveAttribute(R.attr.cpSectionHeight, typedValue, true);
        mSectionHeight = context.getResources().getDimensionPixelSize(typedValue.resourceId);

        context.getTheme().resolveAttribute(R.attr.cpSectionTextSize, typedValue, true);
        mTextSize = context.getResources().getDimensionPixelSize(typedValue.resourceId);

        context.getTheme().resolveAttribute(R.attr.cpSectionTextColor, typedValue, true);
        mTextColor = context.getResources().getColor(typedValue.resourceId);*//*


        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(mBgColor);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);

        mBounds = new Rect();
    }

    public void setData(List<CityItem> data) {
        this.mData = data;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position);
                } else {
                    if (null != mData.get(position).getSortLetters()
                            && !mData.get(position).getSortLetters().equals(mData.get(position - 1).getSortLetters())) {
                        drawSection(c, left, right, child, params, position);
                    }
                }
            }
        }
    }

    private void drawSection(Canvas c, int left, int right, View child,
                             RecyclerView.LayoutParams params, int position) {
        c.drawRect(left,
                child.getTop() - params.topMargin - mSectionHeight,
                right,
                child.getTop() - params.topMargin, mBgPaint);
        mTextPaint.getTextBounds(mData.get(position).getSortLetters(),
                0,
                mData.get(position).getSortLetters().length(),
                mBounds);
        c.drawText(mData.get(position).getSortLetters(),
                child.getPaddingLeft(),
                child.getTop() - params.topMargin - (mSectionHeight / 2 - mBounds.height() / 2),
                mTextPaint);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        if (pos < 0) return;
        if (mData == null || mData.isEmpty()) return;
        String section = mData.get(pos).getSortLetters();
        View child = parent.findViewHolderForLayoutPosition(pos).itemView;

        boolean flag = false;
        if ((pos + 1) < mData.size()) {
            if (null != section && !section.equals(mData.get(pos + 1).getSortLetters())) {
                if (child.getHeight() + child.getTop() < mSectionHeight) {
                    c.save();
                    flag = true;
                    c.translate(0, child.getHeight() + child.getTop() - mSectionHeight);
                }
            }
        }
        c.drawRect(parent.getPaddingLeft(),
                parent.getPaddingTop(),
                parent.getRight() - parent.getPaddingRight(),
                parent.getPaddingTop() + mSectionHeight, mBgPaint);
        mTextPaint.getTextBounds(section, 0, section.length(), mBounds);
        c.drawText(section,
                child.getPaddingLeft(),
                parent.getPaddingTop() + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2),
                mTextPaint);
        if (flag)
            c.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (mData != null && !mData.isEmpty() && position <= mData.size() - 1 && position > -1) {
            if (position == 0) {
                outRect.set(0, mSectionHeight, 0, 0);
            } else {
                if (null != mData.get(position).getSortLetters()
                        && !mData.get(position).getSortLetters().equals(mData.get(position - 1).getSortLetters())) {
                    outRect.set(0, mSectionHeight, 0, 0);
                }
            }
        }
    }


}
*/
