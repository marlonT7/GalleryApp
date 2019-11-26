package com.example.marlon.galleryapp

import com.google.gson.annotations.SerializedName

class Photos(
    @SerializedName("photos")
    val photos: ArrayList<NasaPhoto?>
)
