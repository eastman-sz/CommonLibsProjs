package com.photo.album

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.photo.base.PhotoBaseAdapter
import com.photo.third.UniversalImageLoadTool
import com.sdk.photo.view.R
/**
 * 图片文件夹适配器.
 * @author E
 */
class PhotoFolderAdapter : PhotoBaseAdapter<AlbumInfo> {

    constructor(context: Context , list: List<AlbumInfo>) : super(context , list , R.layout.photo_folder_adapter)

    override fun getConvertView(convertView: View, list: List<AlbumInfo>, position: Int) {
        val imageView = convertView.findViewById<ImageView>(R.id.imageView)
        val info = convertView.findViewById<TextView>(R.id.info)
        val num = convertView.findViewById<TextView>(R.id.num)

        val albumInfo = list[position]

        UniversalImageLoadTool.disPlay(albumInfo?.path_file, imageView, R.drawable.def_loading_square_image)
        info.text = albumInfo?.name_album

        num.text = "( " + albumInfo?.list?.size + " " + "张" + ")"

    }

}