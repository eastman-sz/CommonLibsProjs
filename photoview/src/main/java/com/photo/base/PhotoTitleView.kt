package com.photo.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_sdk_title.view.*

class PhotoTitleView : PhotoBaseRelativeLayoutView {

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs : AttributeSet) : super(context , attrs){
        init()
    }

    override fun initViews() {
        View.inflate(context , R.layout.photo_sdk_title , this)
    }

    override fun initListeners() {
        leftBtnTextView.setOnClickListener {
            onPhotoTitleClickListener?.onLeftBtnClick()
        }
        rightBtnTextView.setOnClickListener {
            onPhotoTitleClickListener?.onRightBtnClick()
        }
    }

    fun setLeftBtnText(text : String){
        leftBtnTextView.text = text
    }

    fun setCenterTitleText(text : String){
        centerTitleTextView.text = text
    }

    fun setRightBtnText(text : String){
        rightBtnTextView.text = text
    }

    fun setRightBtnEnabled(enabled : Boolean){
        rightBtnTextView.isEnabled = enabled
        rightBtnTextView.setTextColor(if (enabled) resources.getColor(R.color.photoSdkTitleTextColor) else resources.getColor(R.color.photoDividerLineColor))
    }

    abstract class OnPhotoTitleClickListener{
        open fun onLeftBtnClick(){}
        open fun onRightBtnClick(){}
    }

    var onPhotoTitleClickListener : OnPhotoTitleClickListener ?= null



}