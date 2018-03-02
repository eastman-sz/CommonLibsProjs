package com.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView,用来解决GridView由于Item高度不一导致显示不完全的问题。
 * @author E
 */
public class GridViewExtend extends GridView {

    public GridViewExtend(Context context) {
		super(context);
	}

	public GridViewExtend(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}