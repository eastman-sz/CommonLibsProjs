package com.sdk.photo.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.photo.album.OnImgSelectResultListener
import com.photo.album.PhotoFolderDialog
import com.photo.third.UniversalImageLoadTool
import com.photo.util.ImgPermissionHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImgPermissionHelper.requestTakePhotoPermissions(this)

        UniversalImageLoadTool.init(this)

        textView.setOnClickListener {

            val dialog = PhotoFolderDialog(this)
            dialog.show()
            dialog.maxNum = 5
            dialog.onImgSelectResultListener = object :OnImgSelectResultListener{
                override fun onResult(imgList: List<String>) {
                    Log.e("photo" , "选中图片: ${imgList.size}")
                }
            }

        }
    }


}
