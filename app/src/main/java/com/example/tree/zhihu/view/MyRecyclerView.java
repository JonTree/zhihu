package com.example.tree.zhihu.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

public class MyRecyclerView extends RecyclerView {


    private ViewTreeObserver.OnScrollChangedListener onScrollChangedListener;

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public MyRecyclerView(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, expandSpec);
    }
}
