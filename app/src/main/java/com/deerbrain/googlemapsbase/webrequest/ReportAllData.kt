package com.deerbrain.googlemapsbase.webrequest

import com.google.gson.annotations.SerializedName

data class ReportAllData (
    @SerializedName("status") val status : String,
    @SerializedName("count") val count : Int,
    @SerializedName("page") val page : Int,
    @SerializedName("rpp") val rpp : Int,
    @SerializedName("results") val results : List<Results>,
    @SerializedName("query") val query : String
        )


data class Results (
    @SerializedName("parcel_id") val parcel_id : String,
    @SerializedName("county_id") val county_id : Int,
    @SerializedName("rausa_id") val rausa_id : Int,
    @SerializedName("county_name") val county_name : String,
    @SerializedName("muni_name") val muni_name : Int,
    @SerializedName("state_abbr") val state_abbr : String,
    @SerializedName("addr_number") val addr_number : Int,
    @SerializedName("addr_street_name") val addr_street_name : String,
    @SerializedName("addr_street_suffix") val addr_street_suffix : String,
    @SerializedName("census_zip") val census_zip : Int,
    @SerializedName("owner") val owner : String,
    @SerializedName("mail_address1") val mail_address1 : String,
    @SerializedName("mail_address3") val mail_address3 : String,
    @SerializedName("muni_id") val muni_id : Int,
    @SerializedName("school_dist_id") val school_dist_id : Int,
    @SerializedName("acreage_deeded") val acreage_deeded : Double,
    @SerializedName("acreage_calc") val acreage_calc : Double,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("longitude") val longitude : Double,
    @SerializedName("geom_as_wkt") val geom_as_wkt : String,
    @SerializedName("robust_id") val robust_id : String,
    @SerializedName("elevation") val elevation : Double
)