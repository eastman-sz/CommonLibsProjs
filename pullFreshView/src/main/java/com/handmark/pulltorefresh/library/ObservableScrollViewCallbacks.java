package com.handmark.pulltorefresh.library;

import com.handmark.pulltorefresh.library.MyListView.ScrollState;

public abstract class ObservableScrollViewCallbacks {
	/**
	 * 滑动回调
	 * @param scrollY 滑动的距离
	 * @param firstScroll 第一次按下滑动时为true，之后为false
	 * @param dragging 按下滑动为true,手指离开或滑动取消为false
	 * @param scrollState 当前滑动的状态,UP\DOWN\STOP
	 */
	public abstract void onScrollChanged(int scrollY, boolean firstScroll,
			boolean dragging ,ScrollState scrollState);

	public void onDownMotionEvent() {
	};

	public void onUpOrCancelMotionEvent(ScrollState scrollState) {
	};
}
