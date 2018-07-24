package com.photo.base

import android.content.Context
import android.os.Bundle
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_common_dialog.*

class PhotoCommonDialog : PhotoBaseDialog {

    var onPhotoCommonDialogClickListener : OnPhotoCommonDialogClickListener ?= null

    constructor(context: Context?) : super(context , R.style.popDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_common_dialog)

        init()
    }

    override fun initListeners() {
        leftBtnTextView.setOnClickListener {
            dismiss()
            onPhotoCommonDialogClickListener?.onClick(0)
        }
        rightBtnTextView.setOnClickListener {
            dismiss()
            onPhotoCommonDialogClickListener?.onClick(1)
        }
    }

    fun setDialogText(content : String , leftBtnText : String , rightBtnText : String){
        contentTextView.text = content
        leftBtnTextView.text = leftBtnText
        rightBtnTextView.text = rightBtnText
    }



}