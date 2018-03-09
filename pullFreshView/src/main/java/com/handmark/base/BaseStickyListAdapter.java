package com.handmark.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;

import com.handmark.stickylistheaders.StickyListHeadersAdapter;

/**
 * PullToRefreshStickyListView的适配器的基类。
 * @author E
 */
public abstract class BaseStickyListAdapter extends BaseAdapter implements
        StickyListHeadersAdapter, SectionIndexer{

	@Override
	public abstract int getCount();

	@Override
	public abstract Object getItem(int position);

	@Override
	public abstract long getItemId(int position);

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);

	@Override
	public abstract View getHeaderView(int position, View convertView, ViewGroup parent);

	@Override
	public abstract Object[] getSections();

	@Override
	public abstract int getPositionForSection(int section);

	@Override
	public abstract int getSectionForPosition(int position);

	@Override
	public abstract long getHeaderId(int position);

}
