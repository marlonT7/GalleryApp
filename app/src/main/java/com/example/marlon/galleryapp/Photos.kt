package com.example.marlon.galleryapp

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

class Photos(
    @SerializedName("photos")
    val photos: ArrayList<NasaPhoto>
){
    fun parseJSON(response: String): NasaPhoto{
        val gson=GsonBuilder().create()
        return gson.fromJson(response,NasaPhoto::class.java)
    }
}
