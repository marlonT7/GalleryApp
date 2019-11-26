package com.example.marlon.galleryapp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName

data class NasaPhoto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("camera")
    val camera: Camera?,
    @SerializedName("img_src")
    val imgSrc: String?,
    @SerializedName("rover")
    val rover: Rover?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Camera::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Rover::class.java.classLoader)
    )
    // Used in GSON convert
    fun parseJOSN(response:String): Camera{
        val gson= GsonBuilder().create()
        return gson.fromJson(response,Camera::class.java)
    }
    // Used in GSON convert
    fun parseJSON(response: String): Rover{
        val gson= GsonBuilder().create()
        return gson.fromJson(response,Rover::class.java)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeParcelable(camera, flags)
        parcel.writeString(imgSrc)
        parcel.writeParcelable(rover, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NasaPhoto> {
        override fun createFromParcel(parcel: Parcel): NasaPhoto {
            return NasaPhoto(parcel)
        }

        override fun newArray(size: Int): Array<NasaPhoto?> {
            return arrayOfNulls(size)
        }
    }

}
