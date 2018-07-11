package com.photo.album

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import com.photo.base.PhotoBaseAdapter
import com.photo.third.UniversalImageLoadTool
import com.sdk.photo.view.R

class PhotoAdapter : PhotoBaseAdapter<PhotoInfo> {

    private var mWidth = 0

    private var showSelector = true

    var onPhotoAdapterClickListener : OnPhotoAdapterClickListener ?= null

    constructor(context: Context , list: List<PhotoInfo>) : super(context , list , R.layout.photo_adapter){
        mWidth = context.resources.displayMetrics.widthPixels/3
    }

    override fun getConvertView(convertView: View, list: List<PhotoInfo>, position: Int) {
        val imageView = convertView.findViewById<ImageView>(R.id.imageView)
        val checkBox = convertView.findViewById<CheckBox>(R.id.checkBox)

        val layoutParams = imageView.layoutParams
        layoutParams.width = mWidth
        layoutParams.height = mWidth
        imageView.layoutParams = layoutParams

        val photoInfo = list[position]
        val choose = photoInfo.choose
        val path_file = photoInfo.path_file

        checkBox.isChecked = choose
        UniversalImageLoadTool.disPlay(path_file, imageView, R.drawable.def_loading_square_image)

        checkBox.visibility = if (showSelector) View.VISIBLE else View.GONE

        checkBox.setOnCheckedChangeListener{ _, b ->
            Log.e("photo" , "checked : $b")
            photoInfo.choose = b

            onPhotoAdapterClickListener?.onCheckedChg(b , photoInfo , position)
        }

        imageView.setOnClickListener {
            onPhotoAdapterClickListener?.onImgClick(photoInfo , position)
        }
    }

    fun setshowSelector(needShow : Boolean){
        this.showSelector = needShow
        notifyDataSetChanged()
    }


}