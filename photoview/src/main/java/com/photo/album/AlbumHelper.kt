package com.photo.album

import android.content.Context
import android.content.DialogInterface

class AlbumHelper {

    companion object {

        fun selectImg(context: Context, maxNum : Int, onImgSelectResultListener : OnImgSelectResultListener, onDismissListener : DialogInterface.OnDismissListener?){
            val dialog = PhotoFolderDialog(context)
            dialog.show()
            dialog.maxNum = maxNum
            dialog.onImgSelectResultListener = onImgSelectResultListener
            dialog.setOnDismissListener {
                onDismissListener?.onDismiss(it)
            }
        }

    }
}