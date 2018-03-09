package com.libs.module.pullfreshview

import android.os.Bundle
import android.widget.ListView
import com.common.base.BaseAppCompactActivitiy
import com.common.base.CommonTitleView
import com.common.libs.proj.R
import com.common.libs.util.ILog
import com.handmark.pulltorefresh.library.OnPullReFreshListener2
import com.handmark.pulltorefresh.library.PullToRefreshBase
import kotlinx.android.synthetic.main.activity_pull_fresh_view.*

class PullFreshViewActivity : BaseAppCompactActivitiy(), OnPullReFreshListener2 {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pull_fresh_view)

        initActivitys()
    }

    override fun initTitle() {
        commontitle_view.setCenterTitleText("PullFreshView")
        commontitle_view.setOnTitleClickListener(object : CommonTitleView.OnTitleClickListener(){
            override fun onLeftBtnClick() {
                onBackPressed()
            }
        })
    }

    override fun initListener() {
        pull_fresh_listview.setOnRefreshListener(this)
    }

    override fun onPullDownToRefresh(refreshView: PullToRefreshBase<ListView>?) {
        ILog.e("---onPullDownToRefresh---")
    }

    override fun onPullUpToRefresh(refreshView: PullToRefreshBase<ListView>?) {
        ILog.e("---onPullUpToRefresh---")
    }

}
