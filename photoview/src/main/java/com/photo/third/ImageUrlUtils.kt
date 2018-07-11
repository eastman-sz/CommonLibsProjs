package com.photo.third

class ImageUrlUtils {

    companion object {

        fun comfirmHttpUrl(url : String) : String{
            if (url.isNullOrEmpty()){
                return url
            }
            if (url.startsWith("/storage/sdcard") || url.startsWith("/storage/")) {
                return "file://$url"
            }
            return url
        }

    }

}