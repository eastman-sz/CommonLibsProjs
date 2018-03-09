package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class MyListView2 extends ListView {
	private int[] mTouchableIds = new int[] {};// 设置可点击的控件
	
	public MyListView2(Context context, AttributeSet attrs, int defStyle){
		super( context, attrs, defStyle );
	}

	public MyListView2(Context context, AttributeSet attrs) {
		super( context, attrs );
	}

	public MyListView2(Context context) {
		super( context );
	}

	public void setTouchableIds(int[] mTouchableIds) {
		this.mTouchableIds = mTouchableIds;
	}

	private float xDistance ;
	private float yDistance ;
	private float lastX ;
	private float lastY ;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            xDistance = yDistance = 0f;
            lastX = event.getX();
            lastY = event.getY();
            break;
        case MotionEvent.ACTION_MOVE:
            final float curX = event.getX();
            final float curY = event.getY();
            xDistance += Math.abs(curX - lastX);
            yDistance += Math.abs(curY - lastY);
            lastX = curX;
            lastY = curY;
            if ((xDistance-yDistance) >= -20) {
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
        	                    return false;
        	                }
        				}
        			}
        		}
            } 
            break;
        }
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
}
