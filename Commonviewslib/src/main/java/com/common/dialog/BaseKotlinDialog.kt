package com.common.dialog

import android.content.Context
import com.common.util.Utils
import com.common.views.lib.ss.R

open class BaseKotlinDialog : BaseDialog{

    constructor(context: Context) : super(context , R.style.common_Dialog)

    override fun show() {
        super.show()

        val attributes = window.attributes
        attributes.width = context.resources.displayMetrics.widthPixels - Utils.dip2px(context , 80f)
        window.attributes = attributes

    }

}