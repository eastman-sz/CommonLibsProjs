package com.photo.album

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.photo.base.OnPhotoCommonDialogClickListener
import com.photo.base.PhotoCommonDialog
import com.photo.base.PhotoFullScreenDialog
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_preview_dialog.*

class PhotoPreviewDialog : PhotoFullScreenDialog {

    private val list = ArrayList<String>()

    var onPhotoPreviewListener : OnPhotoPreviewListener ?= null

    constructor(context: Context) : super(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_preview_dialog)

        init()
    }

    private val onPhotoPreviewAdapterClickListener = object : OnPhotoPreviewAdapterClickListener{
        override fun onDelImg(position: Int) {

            val dialog = PhotoCommonDialog(mContext)
            dialog.show()
            dialog.setDialogText("确定要删除这张图片吗?" , "确定" , "取消")
            dialog.onPhotoCommonDialogClickListener = object : OnPhotoCommonDialogClickListener{
                override fun onClick(item: Int) {
                    if (0 != item){
                        return
                    }

                    Handler(Looper.getMainLooper()).post {
                        val imgList = ArrayList<String>()
                        list.forEachIndexed { index, s ->
                            if (position != index){
                                imgList.add(s)
                            }
                        }

                        if (imgList.isEmpty()){
                            dismiss()

                            onPhotoPreviewListener?.onImgDel(position)
                            return@post
                        }

                        setImgList(imgList , position , true)

                        onPhotoPreviewListener?.onImgDel(position)
                    }
                }
            }
        }
        override fun onTapClick() {

        }
    }

    fun setImgList(imgList : List<String> , position : Int){
        setImgList(imgList , position , false)
    }

    fun setImgList(imgUrl : String?){
        setImgList(imgUrl , false)
    }

    fun setImgList(imgList : List<String> , position : Int , deletable : Boolean){
        list.clear()
        list.addAll(imgList)

        val adapter = PhotoPreviewAdapter(context , list)
        adapter.deletable = deletable
        photoViewPage.adapter = adapter

        adapter.onPhotoPreviewAdapterClickListener = onPhotoPreviewAdapterClickListener

        photoViewPage.setCurrentItem(position , true)
        adapter.notifyDataSetChanged()
    }

    fun setImgList(imgUrl : String? , deletable : Boolean){
        list.clear()
        imgUrl?.let {
            list.add(it)
            val adapter = PhotoPreviewAdapter(context , list)

            photoViewPage.adapter = adapter
            adapter.deletable = deletable
            adapter.onPhotoPreviewAdapterClickListener = onPhotoPreviewAdapterClickListener

            photoViewPage.setCurrentItem(0 , true)
            adapter.notifyDataSetChanged()
        }
    }

/*    *//**
     * 将照片保存到系统相册。
     *//*
    private fun saveImageToLocalAlbum() {
            try {
                val cr = context.contentResolver
                val path = BmpUtil.saveBmpToFile(bmp.get(curr))

                val url = MediaStore.Images.Media.insertImage(cr, path.getAbsolutePath(), "iwy_image", null)
                context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path.getAbsolutePath())))
                var tip = ""
                if (null != url) {
                    tip = "保存成功"
                } else {
                    tip = "保存失败"
                }
                ToastHelper.makeText(context, tip)
            } catch (e: Exception) {
                e.printStackTrace()
                ToastHelper.makeText(context, "保存失败")
            }
    }*/

}