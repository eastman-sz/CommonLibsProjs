package com.photo.base

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

open class PhotoBaseRelativeLayoutView : RelativeLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context , attrs : AttributeSet) : super(context , attrs)

    protected fun init(){
        initViews()
        initListeners()
    }

    open fun initViews(){

    }

    open fun initListeners(){

    }

}