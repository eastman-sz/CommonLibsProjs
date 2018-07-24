package com.photo.album

import android.content.Context

class PhotoPreviewHelper {

    companion object {

        fun imgPreview(context: Context , imgList : List<String> , position : Int){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgList , position)
        }

        fun imgPreview(context: Context , imgUrl : String?){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgUrl)
        }

        fun imgPreview(context: Context , imgList : List<String> , position : Int , deletable : Boolean){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgList , position , deletable)
        }

        fun imgPreview(context: Context , imgUrl : String? , deletable : Boolean){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgUrl , deletable)
        }

        fun imgPreview(context: Context , imgList : List<String> , position : Int , deletable : Boolean , onPhotoPreviewListener : OnPhotoPreviewListener?){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgList , position , deletable)
            dialog.onPhotoPreviewListener = onPhotoPreviewListener
        }

        fun imgPreview(context: Context , imgUrl : String? , deletable : Boolean , onPhotoPreviewListener : OnPhotoPreviewListener?){
            val dialog = PhotoPreviewDialog(context)
            dialog.show()
            dialog.setImgList(imgUrl , deletable)
            dialog.onPhotoPreviewListener = onPhotoPreviewListener
        }

    }

}