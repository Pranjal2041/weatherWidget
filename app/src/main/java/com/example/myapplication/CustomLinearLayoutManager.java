package com.example.myapplication;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomLinearLayoutManager extends LinearLayoutManager {

    private final float mShrinkAmount = 0.2f;
    private final float mShrinkDistance = 1.0f;

    public CustomLinearLayoutManager(Context context,int i,boolean b) {
        super(context,i,b);
    }

    @Override
    public int scrollHorizontallyBy(int dx,  RecyclerView.Recycler recycler,  RecyclerView.State state) {
        // https://stackoverflow.com/questions/49884648/how-to-mimic-view-pager-with-recycler-view
        int orientation = getOrientation();
        if (orientation == HORIZONTAL) {
            int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
            float midpoint = getWidth() / 2.0f;
            float d0 = 0.0f;
            float d1 = mShrinkDistance * midpoint;
            float s0 = 1.0f;
            float s1 = 1.0f - mShrinkAmount;
            // loop through active children and set scale of child
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float childMidpoint = (getDecoratedRight(child) + getDecoratedLeft(child)) / 2.0f;
                float d = Math.min(d1, Math.abs(midpoint - childMidpoint));
                float scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0);
                child.setScaleX(scale);
                child.setScaleY(scale);
            }
            return scrolled;
        } else {
            return 0;
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scrollVerticallyBy(0, recycler, state);
    }
}