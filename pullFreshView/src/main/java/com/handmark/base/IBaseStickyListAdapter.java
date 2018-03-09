package com.handmark.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by E on 2017/9/14.
 */
public abstract class IBaseStickyListAdapter<T extends IGroup> extends BaseStickyListAdapter {

    protected Context context = null;
    protected List<T> list = null;
    private int layoutId = 0;
    private int headerLayoutId = 0;

    private int[] sectionIndices;
    private Object[] sectionsLetters;

    public IBaseStickyListAdapter(Context context , List<T> list , int layoutId , int headerLayoutId){
        this.context = context;
        this.list = list;
        this.layoutId = layoutId;
        this.headerLayoutId = headerLayoutId;

        sectionIndices = getSectionIndices();
        sectionsLetters = getStartingLetters();
    }

    private int[] getSectionIndices(){
        ArrayList<Integer> tempSections = new ArrayList<Integer>();
        int[] tempIndince = new int[tempSections.size()];
        if (list.isEmpty()) {
            return tempIndince;
        }
        int type = list.get(0).getIgroup();
        tempSections.add(0);
        for (int i = 1; i < list.size(); i++) {
            int bType = list.get(i).getIgroup();
            if (type != bType) {
                type = bType;
                tempSections.add(i);
            }
        }
        int[] tempIndi = new int[tempSections.size()];
        for (int i = 0; i < tempSections.size(); i++) {
            tempIndi[i] = tempSections.get(i);
        }
        return tempIndi;
    }

    private Object[] getStartingLetters() {
        Object[] letters = new Object[sectionIndices.length];
        for (int i = 0; i < sectionIndices.length; i++) {
            int content = list.get(i).getIgroup();
            letters[i] = content;
        }
        return letters;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(layoutId , null);
        }
        getConvertView(convertView , list , position);
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(headerLayoutId , null);
        }
        getHeaderConvertView(convertView , list , position);
        return convertView;
    }

    public abstract void getConvertView(View convertView ,List<T> list , int position);

    public abstract void getHeaderConvertView(View convertView ,List<T> list , int position);

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < sectionIndices.length; i++) {
            if (position < sectionIndices[i]) {
                return i - 1;
            }
        }
        return sectionIndices.length - 1;
    }

    @Override
    public int getPositionForSection(int section) {
        if (section >= sectionIndices.length) {
            section = sectionIndices.length -1 ;
        }else if (section < 0) {
            section = 0;
        }
        return sectionIndices[section];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public long getHeaderId(int position) {
        return list.get(position).getIgroup();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public Object[] getSections() {
        return sectionsLetters;
    }

    /**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     * @param listView 要更新的listview
     * @param position 要更新的位置
     */
    public void notifyDataSetChanged(AbsListView listView, int position) {
        position = position + 1;
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }
    }

}
