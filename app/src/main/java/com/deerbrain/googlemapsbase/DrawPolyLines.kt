package com.deerbrain.googlemapsbase

import com.google.android.gms.maps.model.LatLng

interface DrawPolyLines {
    fun drawPolyLine( list:List<LatLng>)
    fun removePolyLine()
}