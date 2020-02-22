package au.com.kbrsolutions.melbournepublictransport.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop

@Entity(tableName = "favorite_stop_table")
data class DatabaseFavoriteStop constructor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name="route_type")
    val routeType: Int,

    @ColumnInfo(name="stop_id")
    val stopId: Int,

    @ColumnInfo(name="location_name")
    val locationName: String,

    val suburb: String,
    val latitude: Double,
    val longitude: Double,

    @ColumnInfo(name="show_in_magnified_view")
    val showInMagnifiedView: Boolean = false
)

fun List<DatabaseFavoriteStop>.asDomainModel(): List<FavoriteStop> {
    return map {
        FavoriteStop(
            id = it.id,
            routeType = it.routeType,
            stopId = it.stopId,
            locationName = it.locationName,
            suburb = it.suburb,
            latitude = it.latitude,
            longitude = it.longitude,
            showInMagnifiedView = it.showInMagnifiedView
        )
    }
}

fun DatabaseFavoriteStop.asDomainModel(): FavoriteStop {
    return FavoriteStop(
        id = this.id,
        routeType = this.routeType,
        stopId = this.stopId,
        locationName = this.locationName,
        suburb = this.suburb,
        latitude = this.latitude,
        longitude = this.longitude,
        showInMagnifiedView = this.showInMagnifiedView
    )
}