package au.com.kbrsolutions.melbournepublictransport.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails

@Entity(tableName = "line_stop_details_table")
data class DatabaseLineStopDetails constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name="route_id")
    val routeType: Int,
    @ColumnInfo(name="line_or_stop_type")
    val lineOrStopType: Int,
    @ColumnInfo(name="line_id")
    val lineId: Int,
    @ColumnInfo(name="stop_id")
    val stopId: Int,
    @ColumnInfo(name="stop_location_or_line_name")
    val stopLocationOrLineName: String,
    val suburb: String? = null,
    val latitude: Double,
    val longitude : Double,
    val distance: Double,
    val favorite: String? = null,
    @ColumnInfo(name="line_name_short")
    val lineNameShort: String? = null,
    @ColumnInfo(name="line_number")
    val lineNumber: String? = null,
    @ColumnInfo(name="show_in_magnified_view")
    val showInMagnifiedView: Boolean)

fun DatabaseLineStopDetails.asDomainModel(): LineStopDetails {
    return LineStopDetails(
            id = this.id,
            routeType = this.routeType,
            lineOrStopType = this.lineOrStopType,
            lineId = this.lineId,
            stopId = this.stopId,
            stopLocationOrLineName = this.stopLocationOrLineName,
            suburb = this.suburb,
            latitude = this.latitude,
            longitude = this.longitude,
            distance = this.distance,
            favorite = this.favorite,
            lineNameShort = this.lineNameShort,
            lineNumber = this.lineNumber,
            showInMagnifiedView = this.showInMagnifiedView
        )
}

fun List<DatabaseLineStopDetails>.asDomainModel(): List<LineStopDetails> {
    return map {
        LineStopDetails(
            id = it.id,
            routeType = it.routeType,
            lineOrStopType = it.lineOrStopType,
            lineId = it.lineId,
            stopId = it.stopId,
            stopLocationOrLineName = it.stopLocationOrLineName,
            suburb = it.suburb,
            latitude = it.latitude,
            longitude = it.longitude,
            distance = it.distance,
            favorite = it.favorite,
            lineNameShort = it.lineNameShort,
            lineNumber = it.lineNumber,
            showInMagnifiedView = it.showInMagnifiedView
        )
    }
}

fun DatabaseLineStopDetails.flipShowInMagnifiedView(): DatabaseLineStopDetails {
    return DatabaseLineStopDetails(
        id = this.id,
        routeType = this.routeType,
        lineOrStopType = this.lineOrStopType,
        lineId = this.lineId,
        stopId = this.stopId,
        stopLocationOrLineName = this.stopLocationOrLineName,
        suburb = this.suburb,
        latitude = this.latitude,
        longitude = this.longitude,
        distance = this.distance,
        favorite = this.favorite,
        lineNameShort = this.lineNameShort,
        lineNumber = this.lineNumber,
        showInMagnifiedView = !this.showInMagnifiedView
    )
}