package com.photo.album

import java.util.HashMap

class ThumbnailsUtil {

    companion object {

        private val hash = HashMap<Int, String>()

        /**
         * 返回value
         * @param key
         * @return
         */
        fun MapgetHashValue(key: Int, defalt: String): String? {
            return if (hash == null || !hash.containsKey(key)) defalt else hash[key]
        }

        /**
         */
        fun put(key: Int, value: String) {
            hash[key] = value
        }

        fun clear() {
            hash.clear()
        }

    }

}