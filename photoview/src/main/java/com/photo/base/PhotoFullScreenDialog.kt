package com.photo.base

import android.app.Dialog
import android.content.Context
import com.sdk.photo.view.R

open class PhotoFullScreenDialog : PhotoBaseDialog {

    constructor(context: Context) : super(context , R.style.photo_dialog)

    override fun show() {
        super.show()

        var params = window.attributes
        params.width = context.resources.displayMetrics.widthPixels
        params.height = context.resources.displayMetrics.heightPixels
        window.attributes = params
    }

}