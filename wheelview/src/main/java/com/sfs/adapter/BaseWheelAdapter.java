package com.sfs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import java.util.List;

public abstract class BaseWheelAdapter<T> extends AbstractWheelAdapter {

    protected Context context = null;
    protected List<T> list = null;
    private int layoutId = 0;

    public BaseWheelAdapter(Context context, List<T> list , int layoutId) {
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(layoutId , null);
        }
        getConvertView(convertView , list , index);
        return convertView;
    }

    public abstract void getConvertView(View convertView ,List<T> list , int position);

    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     * @param listView 要更新的listview
     * @param position 要更新的位置
     */
    public void notifyDataSetChanged(AbsListView listView, int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            getItem(position, view, listView);
        }
    }
}
