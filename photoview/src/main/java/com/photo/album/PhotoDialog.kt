package com.photo.album

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.photo.base.PhotoFullScreenDialog
import com.photo.base.PhotoTitleView
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_dialog.*
/**
 * 图片列表展示窗口。
 * @author E
 */
class PhotoDialog : PhotoFullScreenDialog {

    private val list = ArrayList<PhotoInfo>()
    private var adapter : PhotoAdapter ?= null
    //最大可选择图片数量
    var maxNum = 0
    //回调
    var onImgSelectResultListener : OnImgSelectResultListener ?= null

    constructor(context: Context) : super(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_dialog)

        init()
    }

    override fun initTitle() {
        photoTitleView.setLeftBtnText("返回")
        photoTitleView.setCenterTitleText("")
        photoTitleView.setRightBtnText("完成")
        photoTitleView.setRightBtnEnabled(false)
        photoTitleView.onPhotoTitleClickListener = object : PhotoTitleView.OnPhotoTitleClickListener(){
            override fun onLeftBtnClick() {
                dismiss()
            }
            override fun onRightBtnClick() {
                dismiss()
                //结果回调
                val imgList = ArrayList<String>()
                list.forEach {
                    if (it.choose){
                        val path_file = it.path_file
                        path_file?.let {
                            imgList.add(it)
                        }
                    }
                }

                onImgSelectResultListener?.onResult(imgList)

            }
        }
    }

    override fun initViews() {
        adapter = PhotoAdapter(context , list)
        gridView.adapter = adapter
        gridView.setOnScrollListener(GridviewScrollListener())

        adapter?.onPhotoAdapterClickListener = object : OnPhotoAdapterClickListener{
            override fun onImgClick(photoInfo: PhotoInfo, position: Int) {
                //预览图片
                val imgList = ArrayList<String>()
                list.forEach {
                    val path_file = it.path_file
                    path_file?.let {
                        imgList.add(it)
                    }
                }

                PhotoPreviewHelper.imgPreview(mContext!! , imgList , position)

            }

            override fun onCheckedChg(checked: Boolean , photoInfo: PhotoInfo , position: Int) {

                var count = 0

                list.forEach {
                    if (it.choose){
                        count ++
                    }
                }

                photoTitleView.setRightBtnEnabled(count > 0)

                if (count > maxNum){
                    photoInfo.choose = false

                    adapter?.notifyDataSetChanged(gridView , position)

                    Toast.makeText(mContext , "最多只能选择$maxNum".plus("图片") , Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun setPhotoList(albumName : String? , photoList : List<PhotoInfo>?){
        list.clear()
        albumName?.let {
            val length = it.length
            if (length > 16){
                photoTitleView.setCenterTitleText(albumName.substring(0 , 16).plus("..."))
            }else{
                photoTitleView.setCenterTitleText(albumName)
            }
        }

        photoList?.let {
            list.addAll(photoList)
            adapter?.notifyDataSetChanged()
        }
    }

}