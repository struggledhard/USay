package com.skh.universitysay.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by SKH on 2017/4/17 0017.
 * 自定义Gridview
 * 解决图片显示不完,只显示一行问题
 */

public class NineGridView extends GridView {
    public NineGridView(Context context) {
        super(context);
    }

    public NineGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
