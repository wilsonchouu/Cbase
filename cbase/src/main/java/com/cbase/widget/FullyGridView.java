package com.cbase.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author : zhouyx
 * @date : 2016/4/14
 * @description : 自适应高度GridView（可在渲染少量UI或嵌套ScrollView时使用，因效率极低，避免在渲染大量UI或数据量多时使用）
 */
public class FullyGridView extends GridView {

    public FullyGridView(Context context) {
        super(context);
    }

    public FullyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
