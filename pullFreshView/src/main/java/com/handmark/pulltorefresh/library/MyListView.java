package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class MyListView extends ListView{
	
	private int[] mTouchableIds = new int[] {};// 设置可点击的控件

	/* ========== 计算滑动的距离scrollY,由此判定是上滑还是下滑，并抛出回调接口   ==========
	 * 因为 ListView 对于 getScrollY（）,onScrollChanged(int l, int t, int oldl, int oldt)等其值和参数均为0
	 * 故手动通过滑动每个item的高度，计算滑动的距离.
	 * 通过scrollY可判定下拉、上滑状态.
	 */
	//存储所游览到的item高度数据,其结构类似于HashMap<Integer,Integer>,但内部使用二分法排序,google官方推荐使用
	private SparseIntArray mChildrenHeights;
	//上一次scrollY
	private int mPrevScrollY;
	//当前滑动的距离
	private int mScrollY;
	//滑动状态,上滑\下滑\停止
	private ScrollState mScrollState;
	private boolean mFirstScroll;
	private boolean mDragging;
	 
	private OnScrollListener mOriginalScrollListener;
	private ObservableScrollViewCallbacks mCallbacks;
	
	public MyListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super( context, attrs, defStyle );
		initScrollListener();
	}

	public MyListView(Context context, AttributeSet attrs)
	{
		super( context, attrs );
		initScrollListener();
	}

	public MyListView(Context context)
	{
		super( context );
		initScrollListener();
	}

	public void setTouchableIds(int[] mTouchableIds) {
		this.mTouchableIds = mTouchableIds;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		int[] location = new int[2];
		int x = (int) event.getX();
		int y = (int) event.getY();
		this.getLocationOnScreen(location);
		x += location[0];
		y += location[1];
		if(mTouchableIds != null) {
			int len = mTouchableIds.length;
			for(int i=0;i<len;i++) {
				View view = findViewById(mTouchableIds[i]);
				if(view!=null) {
				    Rect rect = getRectOnscreen(view);
	                if(rect.contains(x, y)) {
	                    //如果事件发生在目标控件上，将事件分发给该控件，并返回false,后续事件将交由该控件处理;
	                    if(MotionEvent.ACTION_DOWN==event.getAction()) {
	                        view.dispatchTouchEvent(event);
	                    }
	                    return false;
	                }
				}
			}
		}
		/*if(event.getAction() == MotionEvent.ACTION_DOWN && mHandleId != 0) {
			View view = findViewById(mHandleId);
			Rect rect = getRectOnscreen(view);
			if(rect.contains(x, y)) {
				//交由系统处理;
			}else {
				return false;
			}
		}*/
		return super.onInterceptTouchEvent(event);
	}
	
	public Rect getRectOnscreen(View view) {
		Rect rect = new Rect();
		int[] location = new int[2];
		View parent = view;
		if(view.getParent() instanceof View) {
			parent = (View)view.getParent();
		}
		parent.getLocationOnScreen(location);
		view.getHitRect(rect);
		rect.offset(location[0], location[1]);
		return rect;
	}
	
	public enum ScrollState{
		STOP,
	    UP,
	    DOWN,
	}
	
	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// Don't set l to super.setOnScrollListener().
		// l receives all events through mScrollListener.
		mOriginalScrollListener = l;
	}
	
	/**
	 * 滑动回调接口,若MyListView不是直接被布局在xml文件里，需要先调用initScrollListener().
	 * @param listener
	 */
	public void setScrollViewCallbacks(ObservableScrollViewCallbacks listener) {
        mCallbacks = listener;
    }
	
	/**
	 * 当前滑动距离
	 * @return
	 */
	public int getCurrentScrollY() {
        return mScrollY;
    }
	 
	public void initScrollListener() {
		mChildrenHeights = new SparseIntArray();
		super.setOnScrollListener(mScrollListener);
	}
	 
	private OnScrollListener mScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if (mOriginalScrollListener != null) {
				mOriginalScrollListener.onScrollStateChanged(view, scrollState);
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if (mOriginalScrollListener != null) {
				mOriginalScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
			// AbsListView#invokeOnItemScrollListener calls onScrollChanged(0, 0, 0, 0)
			// on Android 4.0+, but Android 2.3 is not. (Android 3.0 is unknown)
			// So call it with onScrollListener.
			onScrollChanged();
		}
		
	};
	
	private void onScrollChanged() {
		if (mCallbacks != null) {
			if (getChildCount() > 0) {
				int scrolledChildrenHeight = 0;
				//可见区域第一个item的位置
				int firstVisiblePosition = getFirstVisiblePosition();
				//存储可见区域item的高度
				for (int i = getFirstVisiblePosition(), j = 0; i <= getLastVisiblePosition(); i++, j++) {
					if (mChildrenHeights.indexOfKey(i) < 0 || getChildAt(j).getHeight() != mChildrenHeights.get(i)) {
						mChildrenHeights.put(i, getChildAt(j).getHeight());
					}
				}
				//可见区域第一个item
				View firstVisibleChild = getChildAt(0);
				if (firstVisibleChild != null) {
					/*if (mPrevFirstVisiblePosition < firstVisiblePosition) {
						//上滑,scrollY 渐渐变大
						//skipped,所完全跃过item的高度
						int skippedChildrenHeight = 0;
						//一次性滑动，即一次性滑过多个item
						if (firstVisiblePosition - mPrevFirstVisiblePosition != 1) {
							//循环从first----->preview,计算一次完全跳过的 item 高度
							for (int i = firstVisiblePosition - 1; i > mPrevFirstVisiblePosition; i--) {
								if (0 < mChildrenHeights.indexOfKey(i)) {
									skippedChildrenHeight += mChildrenHeights.get(i);
								}
							}
						}
						
						//计算总的高度,即累计当前getChildAt(0)和上一次之和
						mPrevScrolledChildrenHeight += mPrevFirstVisibleChildHeight + skippedChildrenHeight;
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					} else if (firstVisiblePosition < mPrevFirstVisiblePosition) {
						//下拉,scrollY 渐渐变小
						int skippedChildrenHeight = 0;
						if (mPrevFirstVisiblePosition - firstVisiblePosition != 1) {
							for (int i = mPrevFirstVisiblePosition - 1; i > firstVisiblePosition; i--) {
								if (0 < mChildrenHeights.indexOfKey(i)) {
									skippedChildrenHeight += mChildrenHeights.get(i);
								} 
							} 
						}
						for (int i = 0; i < firstVisiblePosition; i++) {
							scrolledChildrenHeight += mChildrenHeights.get(i);
						}
						mPrevScrolledChildrenHeight -= firstVisibleChild.getHeight() + skippedChildrenHeight;
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					} else if (firstVisiblePosition == 0) {
						mPrevFirstVisibleChildHeight = firstVisibleChild.getHeight();
					}
					
					if (mPrevFirstVisibleChildHeight < 0) {
						mPrevFirstVisibleChildHeight = 0;
					}*/
					
					
					//计算完全滑过item的高度，不包含当前可见区域第一个滑动的item
					for (int i = 0; i < firstVisiblePosition; i++) {
						scrolledChildrenHeight += mChildrenHeights.get(i);
					}
					
					//计算滑动的距离，需要减去当前可见区域第一个item距离顶部的距离
					mScrollY = scrolledChildrenHeight - firstVisibleChild.getTop();
					
					if (mPrevScrollY < mScrollY) {
						//down
						mScrollState = ScrollState.UP;
					} else if (mScrollY < mPrevScrollY) {
						//up
						mScrollState = ScrollState.DOWN;
					} else {
						mScrollState = ScrollState.STOP;
					}
					//回调抛出
					mCallbacks.onScrollChanged(mScrollY, mFirstScroll, mDragging,mScrollState);
					if (mFirstScroll) {
						mFirstScroll = false;
					}
					mPrevScrollY = mScrollY;
				} 
			}
		}
	}

}
