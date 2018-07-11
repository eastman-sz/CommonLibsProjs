package com.photo.third;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 
 * @author E
 */
public class IwyScrollListener implements OnScrollListener {
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		UniversalImageLoadTool.onScrollStateChanged(scrollState);
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}
}
