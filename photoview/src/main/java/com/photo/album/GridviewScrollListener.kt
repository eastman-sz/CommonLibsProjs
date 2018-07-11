package com.photo.album

import android.widget.AbsListView
import com.photo.third.UniversalImageLoadTool

class GridviewScrollListener : AbsListView.OnScrollListener {

    override fun onScroll(p0: AbsListView?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onScrollStateChanged(p0: AbsListView?, scrollState: Int) {
        UniversalImageLoadTool.onScrollStateChanged(scrollState)
    }

}