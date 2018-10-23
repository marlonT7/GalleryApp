package com.example.marlon.galleryapp

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Rover(
    @SerializedName("landing_date")
    val landingDate: String,
    @SerializedName("launch_date")
    val launchDate: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(landingDate)
        parcel.writeString(launchDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rover> {
        override fun createFromParcel(parcel: Parcel): Rover {
            return Rover(parcel)
        }

        override fun newArray(size: Int): Array<Rover?> {
            return arrayOfNulls(size)
        }
    }
}