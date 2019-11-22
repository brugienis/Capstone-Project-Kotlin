package au.com.kbrsolutions.melbournepublictransport.domain

data class Departure(
    val id: Int,
    val stopId: Int?,
    val routeId: Int?,
    val runId: Int?,
    val departureTimeUtc: String?,
    val departureDayMonthNoLeadingZeros: String?,
    val departureTimeHourMinutes: String?,
    val displayDepartureDtTm: String?,
    val atPlatform: Boolean?,
    val platformNumber: String?,
    // Below is a String containing Int Ids from List<int> comma separated
    var oneStopDisruptionsIdsList: String?,
    val routeType: Int,
    val directionName: String?,
    val destinationName: String?,
    val lineNumber: String?,
    val lineShortName: String?,
    val runType: String?,
    val status: Int?,
    val isRealTimeInfo: Boolean,
    val showInMagnifiedView: Boolean = false,
    val rowInsertTime: Long
)