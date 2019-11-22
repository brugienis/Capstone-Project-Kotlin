package au.com.kbrsolutions.melbournepublictransport.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LatLngDetails(
    val latitude: Double,
    val longitude: Double): Parcelable