package com.photo.album

import android.content.Context
import android.os.Environment
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.nostra13.universalimageloader.core.assist.FailReason
import com.photo.gesture.PhotoView
import com.photo.third.ImageLoadListener
import com.photo.third.UniversalImageLoadTool
import com.sdk.photo.view.R

class PhotoPreviewAdapter : PagerAdapter {

    private var context: Context? = null
    private var list: List<String>? = null

    var deletable = false

    var onPhotoPreviewAdapterClickListener : OnPhotoPreviewAdapterClickListener ?= null

    constructor(context: Context , list: List<String>){
        this.context = context
        this.list = list
    }

    override fun getCount(): Int {
        return list?.size!!
    }

    override fun isViewFromObject(view: View, p1: Any): Boolean {
        return view == p1
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.photo_preview_adapter, container, false)
        val photoView = view.findViewById<PhotoView>(R.id.photoView)
        photoView.isClickable = true
        val delImageview = view.findViewById<ImageView>(R.id.delImageview)

        delImageview.visibility = if (deletable) View.VISIBLE else View.GONE

        var imageUrl = list!![position]
        imageUrl = if (imageUrl.contains("small_")) imageUrl.replace("small_" , "") else imageUrl
        imageUrl = if (imageUrl.contains("th_")) imageUrl.replace("th_" , "") else imageUrl

        if (imageUrl.contains(Environment.getExternalStorageDirectory().path) && !imageUrl.startsWith("file:")) {
            imageUrl = "file://$imageUrl"
        }

        UniversalImageLoadTool.disPlay(imageUrl , photoView , R.drawable.def_loading_square_image , object : ImageLoadListener(){
            override fun onLoadingFailed(arg0: String?, arg1: View?, arg2: FailReason?) {

                var imageUrl = list!![position]
                UniversalImageLoadTool.disPlay(imageUrl , photoView , R.drawable.def_loading_square_image)

            }
        })

        container.addView(view, 0)

        photoView.setOnViewTapListener { view, x, y ->
            onPhotoPreviewAdapterClickListener?.onTapClick()
        }
        delImageview.setOnClickListener {
            onPhotoPreviewAdapterClickListener?.onDelImg(position)
        }
        return view
    }




}