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

    }

}