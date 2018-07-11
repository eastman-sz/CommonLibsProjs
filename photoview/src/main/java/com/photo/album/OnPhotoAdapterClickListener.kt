package com.photo.album

interface OnPhotoAdapterClickListener {

    fun onImgClick(photoInfo : PhotoInfo , position : Int)

    fun onCheckedChg(checked : Boolean , photoInfo : PhotoInfo , position : Int)
}