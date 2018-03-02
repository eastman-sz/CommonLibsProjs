package com.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自定义ListView,用来解决listView由于Item高度不一导致显示不完全的问题。
 * @author E
 */
public class ItemExpandListview extends ListView {

	public ItemExpandListview(Context context) {
		super(context);
	}

	public ItemExpandListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ItemExpandListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
	            MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
