package au.com.kbrsolutions.melbournepublictransport.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LineStopDetails(
    val id: Int,
    val routeType: Int,
    val lineOrStopType: Int,
    val lineId: Int,
    val stopId: Int,
    val stopLocationOrLineName: String,
    val suburb: String? = null,
    val latitude: Double,
    val longitude : Double,
    val distance: Double,
    val favorite: String? = null,
    val lineNameShort: String? = null,
    val lineNumber: String? = null,
    val showInMagnifiedView: Boolean): Parcelable {

    companion object {
        const val LINE_DETAILS = 0
        const val STOP_DETAILS = 1
    }
}