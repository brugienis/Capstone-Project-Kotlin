package au.com.kbrsolutions.melbournepublictransport.domain

import au.com.kbrsolutions.melbournepublictransport.utilities.TRAIN_ROUTE_TYPE

data class FavoriteStop(
    val id: Int,
    val routeType: Int,
    val stopId: Int,
    val locationName: String,
    val suburb: String,
    val latitude: Double,
    val longitude: Double,
    val showInMagnifiedView: Boolean) {
    val isTrainStop = routeType == TRAIN_ROUTE_TYPE

}