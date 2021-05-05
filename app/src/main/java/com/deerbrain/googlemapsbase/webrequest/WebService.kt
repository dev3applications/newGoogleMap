package com.deerbrain.googlemapsbase.webrequest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WebService {

    @GET
     fun getDataFromServer(@Url url:String): Call<ReportAllData>
}