package com.photo.album

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import com.photo.base.PhotoFullScreenDialog
import com.photo.base.PhotoTitleView
import com.sdk.photo.view.R
import kotlinx.android.synthetic.main.photo_folder_dialog.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.HashMap
/**
 * 图片文件夹弹出窗。
 * @author E
 */
class PhotoFolderDialog : PhotoFullScreenDialog {

    val list = ArrayList<AlbumInfo>()
    private var adapter : PhotoFolderAdapter ?= null
    //最大可选择图片数量
    var maxNum = 0
    //回调
    var onImgSelectResultListener : OnImgSelectResultListener ?= null

    constructor(context: Context) : super(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_folder_dialog)

        init()
    }

    override fun initTitle() {
        photoTitleView.setCenterTitleText("相册")
        photoTitleView.onPhotoTitleClickListener = object : PhotoTitleView.OnPhotoTitleClickListener(){
            override fun onLeftBtnClick() {
                dismiss()
            }
        }

    }

    override fun initViews() {
        adapter = PhotoFolderAdapter(context , list)
        listView.adapter = adapter

        getAlbums()

        listView.setOnItemClickListener { _, _, position, _ ->
            val albumInfo = list[position]

            val dialog = PhotoDialog(mContext!!)
            dialog.show()
            dialog.setPhotoList(albumInfo.name_album , albumInfo.list)
            dialog.maxNum = maxNum
            dialog.onImgSelectResultListener = object : OnImgSelectResultListener{
                override fun onResult(imgList: List<String>) {
                    dismiss()
                    onImgSelectResultListener?.onResult(imgList)
                }
            }
        }
    }

    private fun getAlbums(){
        doAsync {
            val resultList = ArrayList<AlbumInfo>()

            //获取缩略图
            val contentResolver = context.contentResolver
            ThumbnailsUtil.clear()
            val projection = arrayOf(MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA)
            val cur = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null)

            if (null != cur && cur.moveToFirst()) {
                var image_id: Int
                var image_path: String
                val image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID)
                val dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA)
                do {
                    image_id = cur.getInt(image_idColumn)
                    image_path = cur.getString(dataColumn)
                    ThumbnailsUtil.put(image_id, "file://$image_path")
                } while (cur.moveToNext())
            }

            //获取原图
            val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "date_modified DESC")

            val _path = "_data"
            val _album = "bucket_display_name"

            val myhash = HashMap<String, AlbumInfo>()
            var albumInfo: AlbumInfo? = null
            var photoInfo: PhotoInfo? = null
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    var index = 0
                    val _id = cursor.getInt(cursor.getColumnIndex("_id"))
                    val path = cursor.getString(cursor.getColumnIndex(_path))
                    val album = cursor.getString(cursor.getColumnIndex(_album))
                    val stringList = java.util.ArrayList<PhotoInfo>()
                    photoInfo = PhotoInfo()
                    if (myhash.containsKey(album)) {
                        albumInfo = myhash.remove(album)
                        if (resultList.contains(albumInfo))
                            index = resultList.indexOf(albumInfo)
                        photoInfo.image_id = _id
                        photoInfo.path_file = ("file://$path")
                        photoInfo.path_absolute = (path)
                        albumInfo!!.list?.add(photoInfo)
                        resultList.set(index, albumInfo)
                        myhash[album] = albumInfo
                    } else {
                        albumInfo = AlbumInfo()
                        stringList.clear()
                        photoInfo.image_id = (_id)
                        photoInfo.path_file = ("file://$path")
                        photoInfo.path_absolute = (path)
                        stringList.add(photoInfo)
                        albumInfo.image_id = (_id)
                        albumInfo.path_file = ("file://$path")
                        albumInfo.path_absolute = (path)
                        albumInfo.name_album = (album)
                        albumInfo.list = stringList
                        resultList.add(albumInfo)
                        myhash[album] = albumInfo
                    }
                } while (cursor.moveToNext())
            }

            uiThread {
                list.addAll(resultList)

                adapter?.notifyDataSetChanged()
            }
        }
    }


}