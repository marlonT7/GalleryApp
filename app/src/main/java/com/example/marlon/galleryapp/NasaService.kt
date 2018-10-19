package com.example.marlon.galleryapp


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface NasaService {
    @GET("photos")
    fun getPhotos(@Query("sol") sol: Int, @Query("page") page: Int, @Query("api_key") key: String): Call<Photos>
}