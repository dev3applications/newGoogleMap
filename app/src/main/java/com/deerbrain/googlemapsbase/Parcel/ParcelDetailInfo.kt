package com.deerbrain.googlemapsbase.Parcel

import com.google.android.gms.maps.model.LatLng
import java.io.StringBufferInputStream

data class ParcelDetailInfo (
    val ownerName: String,
    val fullAddress: String,
    val address1: String,
    val address2: String,
    val calcAcres: String,
    val deedAcres: String,
    val shapeFile: String,
    val shape2D: ArrayList<ArrayList<LatLng>>


)


