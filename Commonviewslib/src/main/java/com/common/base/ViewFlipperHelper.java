package com.common.base;

import android.view.View;
import android.widget.ViewFlipper;

/**
 * ViewFlipper显示View的辅助类。
 * @author E
 */
public class ViewFlipperHelper {

	private static ViewFlipperHelper viewFlipperHelper = null;
	
	public static ViewFlipperHelper getInstance(){
		if (null == viewFlipperHelper) {
			viewFlipperHelper = new ViewFlipperHelper();
		}
		return viewFlipperHelper;
	}
	
	public void setDisplayView(ViewFlipper viewFlipper ,View displayView){
		int displayId = displayView.getId();
		View view =  viewFlipper.findViewById(displayId);
		if (null == view) {
			int count = viewFlipper.getChildCount();
			viewFlipper.addView(displayView,count);
			viewFlipper.setDisplayedChild(count);
			return;
		}
		int count = viewFlipper.getChildCount();
		for (int i = 0; i < count; i++) {
			View childView = viewFlipper.getChildAt(i);
			int childId = childView.getId();
			if (displayId == childId){
				viewFlipper.setDisplayedChild(i);
				return;
			}
		}
	}
	
	public void setDisplayView(ViewFlipper viewFlipper ,int viewId){
		View view =  viewFlipper.findViewById(viewId);
		if (null == view) {
			return;
		}
		int count = viewFlipper.getChildCount();
		for (int i = 0; i < count; i++){
			View childView = viewFlipper.getChildAt(i);
			int childId = childView.getId();
			if (viewId == childId){
				viewFlipper.setDisplayedChild(i);
				return;
			}
		}
	}
	
}
