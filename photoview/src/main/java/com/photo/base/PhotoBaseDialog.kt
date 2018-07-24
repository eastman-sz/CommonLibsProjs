package com.photo.base

import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.WindowManager
import com.sdk.photo.view.R

open class PhotoBaseDialog : Dialog{

    protected var mContext : Context ?= null

    constructor(context: Context?) : super(context){
        this.mContext = context
        initSystemBar()
    }

    constructor(context: Context? , themeResId : Int) : super(context , themeResId){
        this.mContext = context
        initSystemBar()
    }

    constructor(context: Context? , cancelable : Boolean, cancelListener : DialogInterface.OnCancelListener)
            : super(context , cancelable , cancelListener){
        this.mContext = context
        initSystemBar()
    }

    protected fun init(){
        initTitle()
        initViews()
        initListeners()
    }

    open fun initTitle(){

    }

    open fun initViews(){

    }

    open fun initListeners(){

    }

    //这个会导致键盘遮挡输入框
    private fun initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true)
        }

        val tintManager = SystemBarTintManager(mContext as Activity)
        tintManager.isStatusBarTintEnabled = false
        tintManager.setStatusBarTintResource(R.color.transparent)
    }

    @TargetApi(19)
    private fun setTranslucentStatus(on: Boolean) {
        val win = window
        val winParams = win!!.attributes
        val bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }
}