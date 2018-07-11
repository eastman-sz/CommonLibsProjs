package com.photo.album

import android.content.Context
import android.os.Bundle
import com.photo.base.PhotoFullScreenDialog
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_preview_dialog.*

class PhotoPreviewDialog : PhotoFullScreenDialog {

    private val list = ArrayList<String>()
    private var adapter : PhotoPreviewAdapter ?= null

    constructor(context: Context) : super(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_preview_dialog)

        init()
    }

    override fun initViews() {
        adapter = PhotoPreviewAdapter(context , list)
        photoViewPage.adapter = adapter

        adapter?.onPhotoPreviewAdapterClickListener = object : OnPhotoPreviewAdapterClickListener{
            override fun onDelImg(position: Int) {

            }
            override fun onTapClick() {

            }
        }
    }

    fun setImgList(imgList : List<String> , position : Int){
        list.clear()
        list.addAll(imgList)
        photoViewPage.setCurrentItem(position , true)
        adapter?.notifyDataSetChanged()
    }

    fun setImgList(imgUrl : String?){
        list.clear()
        imgUrl?.let {
            list.add(it)
            photoViewPage.setCurrentItem(0 , true)
            adapter?.notifyDataSetChanged()
        }
    }

}