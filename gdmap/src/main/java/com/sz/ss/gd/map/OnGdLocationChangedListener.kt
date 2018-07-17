package com.sz.ss.gd.map

interface OnGdLocationChangedListener {

    fun onLocationChanged(latitude : Double , longitude : Double , jsonInfo : String?)

}