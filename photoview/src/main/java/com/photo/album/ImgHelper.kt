package com.photo.album

import android.content.Context
import android.content.Intent

class ImgHelper {

    companion object {

        fun selectImg(context: Context , maxNum : Int , onImgSelectResultListener : OnImgSelectResultListener){
            ImgConfig.onImgSelectResultListener = onImgSelectResultListener

            context.startActivity(Intent(context , ImgActivity::class.java)
                    .putExtra("toType" , 1)
                    .putExtra("maxNum" , maxNum)
            )
        }

        fun takePhoto(context: Context , onImgSelectResultListener : OnImgSelectResultListener){
            ImgConfig.onImgSelectResultListener = onImgSelectResultListener

            context.startActivity(Intent(context , ImgActivity::class.java)
                    .putExtra("toType" , 2)
            )
        }

        fun imgPreview(context: Context , imgList : List<String> , position : Int){
            PhotoPreviewHelper.imgPreview(context , imgList , position)
        }

        fun imgPreview(context: Context , imgUrl : String?){
            PhotoPreviewHelper.imgPreview(context , imgUrl)
        }

    }

}