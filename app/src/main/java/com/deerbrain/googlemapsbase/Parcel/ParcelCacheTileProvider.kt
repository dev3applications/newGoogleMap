package com.deerbrain.googlemapsbase.Parcel

import com.deerbrain.googlemapsbase.Parcel.ParcelCacheManager
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider

class ParcelCacheTileProvider : TileProvider {
    override fun getTile(x: Int, y: Int, z: Int): Tile? {
        return ParcelCacheManager.getTile(x, y, z)
    }


}