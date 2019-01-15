package com.photo.album

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.photo.util.PhotoImgpHelper
import com.photo.util.ImgPermissionHelper
/**
 * 相册和拍照。
 * @author E
 */
class ImgActivity : Activity() {

    private val TAKE_PICTURE = 10088

    private var toType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //权限判断
        toType = intent.getIntExtra("toType" , 1)
        when(toType){
            1 -> {
                //图片
                val hasPermission = ImgPermissionHelper.hasValidImgPermission(this)
                when(hasPermission){
                    true -> {
                        toDoActions()
                    }

                    else ->{
                        ImgPermissionHelper.requestImgPermissions(this)
                    }
                }
            }

            2 -> {
                //拍照
                val hasPermission = ImgPermissionHelper.hasValidTakePhotoPermission(this)
                when(hasPermission){
                    true -> {
                        toDoActions()
                    }

                    else ->{
                        ImgPermissionHelper.requestTakePhotoPermissions(this)
                    }
                }
            }
        }
    }

    private fun toDoActions(){
        when(toType){
            1 -> {
                imgSelect()
            }

            2 ->{
                takePhoto()
            }
        }
    }

    private fun imgSelect(){
        val maxNum = intent.getIntExtra("maxNum" , 0)

        AlbumHelper.selectImg(this , maxNum , object : OnImgSelectResultListener{
            override fun onResult(imgList: List<String>) {

                ImgConfig.onImgSelectResultListener?.onResult(imgList)
            }
        } , DialogInterface.OnDismissListener {
            finish()
        })
    }

    private fun takePhoto(){
        startActivityForResult(Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (TAKE_PICTURE != requestCode) {
            finish()
            return
        }
        if (null == data) {
            finish()
            return
        }

        //缩略图
        val bundle = data.extras
        if (null == bundle) {
            onTakePhotosFailed()
            return
        }
        val `object` = bundle.get("data")
        if (null == `object`) {
            onTakePhotosFailed()
            return
        }
        val bmp = `object` as Bitmap
        if (null == bmp) {
            onTakePhotosFailed()
            return
        }

        val path = PhotoImgpHelper.saveBitmapToFile(bmp)
        val degree = PhotoImgpHelper.readPictureDegree(path)
        val bitmap = PhotoImgpHelper.rotateBitmap(bmp, degree)

        //将图片转为合适大小的图片并存入到手机上
        val bmpPath = PhotoImgpHelper.saveBitmapToFile(bitmap)
        if (null == bmpPath) {
            onTakePhotosFailed()
            return
        }

        Log.e("photo" , "拍照成功，地址: $bmpPath")

        finish()

        //回调
        val imgList = ArrayList<String>()
        imgList.add(bmpPath)

        ImgConfig.onImgSelectResultListener?.onResult(imgList)
    }


    private fun onTakePhotosFailed() {
        Toast.makeText(this , "拍照失败" , Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1560 ->{
                //拍照权限判断
                val hasPermission = ImgPermissionHelper.hasValidTakePhotoPermission(this)
                when(hasPermission){
                    true -> {
                        toDoActions()
                    }

                    else ->{
                        finish()
                    }
                }
            }

            1570 ->{
                //相册选择图片权限判断
                val hasPermission = ImgPermissionHelper.hasValidImgPermission(this)
                when(hasPermission){
                    true -> {
                        toDoActions()
                    }

                    else ->{
                        finish()
                    }
                }
            }
        }
    }

}
