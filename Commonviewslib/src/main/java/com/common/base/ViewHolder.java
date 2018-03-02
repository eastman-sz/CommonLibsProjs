package com.common.base;

import android.util.SparseArray;
import android.view.View;

/**
 * View缓存器。
 * @author E
 */
public class ViewHolder {

	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(View convertView, int id) {
		SparseArray<View> holder = (SparseArray<View>) convertView.getTag();
		if (null == holder) {
			holder = new SparseArray<View>();
			convertView.setTag(holder);
		}
		View childView = holder.get(id);
		if (null == childView) {
			childView = convertView.findViewById(id);
			holder.put(id, childView);
		}
		return (T) childView;
	}
}
