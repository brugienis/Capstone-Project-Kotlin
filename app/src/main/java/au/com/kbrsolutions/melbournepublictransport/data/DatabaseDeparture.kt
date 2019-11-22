package au.com.kbrsolutions.melbournepublictransport.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import au.com.kbrsolutions.melbournepublictransport.domain.Departure

@Entity(tableName = "departure_table")
data class DatabaseDeparture constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    val stopId: Int?,
    @ColumnInfo(name="route_id")
    val routeId: Int?,
    @ColumnInfo(name="run_id")
    val runId: Int?,
    @ColumnInfo(name="databaseDeparture-Time_utc")
    val departureTimeUtc: String?,
    @ColumnInfo(name="departure_day_month_no_leading_zeros")
    val departureDayMonthNoLeadingZeros: String?,
    @ColumnInfo(name="departure_time_hour_minutes")
    val departureTimeHourMinutes: String?,
    @ColumnInfo(name="displayDeparture_dt_tm")
    val displayDepartureDtTm: String?,
    @ColumnInfo(name="at_platform")
    val atPlatform: Boolean?,
    @ColumnInfo(name="platform_number")
    val platformNumber: String?,
    @ColumnInfo(name="one_stop_disruptions_ids_list")
    // Below is a String containing Int Ids from List<int> comma separated
    var oneStopDisruptionsIdsList: String?,
    @ColumnInfo(name="route_type")
    val routeType: Int,
    @ColumnInfo(name="directionName")
    val directionName: String?,
    @ColumnInfo(name="destination_name")
    val destinationName: String?,
    @ColumnInfo(name="lineNumber")
    val lineNumber: String?,
    @ColumnInfo(name="line_short_name")
    val lineShortName: String?,
    @ColumnInfo(name="run_type")
    val runType: String?,
    @ColumnInfo(name="status")
    val status: Int?,
    @ColumnInfo(name="isReal_time_info")
    val isRealTimeInfo: Boolean,
    @ColumnInfo(name="show_in_magnified_view")
    val showInMagnifiedView: Boolean = false,
    @ColumnInfo(name = "row_insert_time")
    val rowInsertTime: Long = System.currentTimeMillis()
)

fun List<DatabaseDeparture>.asDomainModel(): List<Departure> {
    return map {
        Departure(
            id = it.id,
            stopId = it.stopId,
            routeId = it.routeId,
            runId = it.runId,
            departureTimeUtc = it.departureTimeUtc,
            departureDayMonthNoLeadingZeros = it.departureDayMonthNoLeadingZeros,
            departureTimeHourMinutes = it.departureTimeHourMinutes,
            displayDepartureDtTm = it.displayDepartureDtTm,
            atPlatform = it.atPlatform,
            platformNumber = it.platformNumber,
            // Below is a String containing Int Ids from List<int> comma separated
            oneStopDisruptionsIdsList = it.oneStopDisruptionsIdsList,
            routeType = it.routeType,
            directionName = it.directionName,
            destinationName = it.destinationName,
            lineNumber = it.lineNumber,
            lineShortName = it.lineShortName,
            runType = it.runType,
            status = it.status,
            isRealTimeInfo = it.isRealTimeInfo,
            showInMagnifiedView = it.showInMagnifiedView,
            rowInsertTime = it.rowInsertTime
        )
    }
}

fun DatabaseDeparture.asDomainModel(): Departure {
    return Departure(
        id = this.id,
        stopId = this.stopId,
        routeId = this.routeId,
        runId = this.runId,
        departureTimeUtc = this.departureTimeUtc,
        departureDayMonthNoLeadingZeros = this.departureDayMonthNoLeadingZeros,
        departureTimeHourMinutes = this.departureTimeHourMinutes,
        displayDepartureDtTm = this.displayDepartureDtTm,
        atPlatform = this.atPlatform,
        platformNumber = this.platformNumber,
        // Below is a String containing Int Ids from List<int> comma separated
        oneStopDisruptionsIdsList = this.oneStopDisruptionsIdsList,
        routeType = this.routeType,
        directionName = this.directionName,
        destinationName = this.destinationName,
        lineNumber = this.lineNumber,
        lineShortName = this.lineShortName,
        runType = this.runType,
        status = this.status,
        isRealTimeInfo = this.isRealTimeInfo,
        showInMagnifiedView = this.showInMagnifiedView,
        rowInsertTime = this.rowInsertTime
    )
}

fun DatabaseDeparture.flipShowInMagnifiedView(): DatabaseDeparture {
    return DatabaseDeparture(
        id = this.id,
        stopId = this.stopId,
        routeId = this.routeId,
        runId = this.runId,
        departureTimeUtc = this.departureTimeUtc,
        departureDayMonthNoLeadingZeros = this.departureDayMonthNoLeadingZeros,
        departureTimeHourMinutes = this.departureTimeHourMinutes,
        displayDepartureDtTm = this.displayDepartureDtTm,
        atPlatform = this.atPlatform,
        platformNumber = this.platformNumber,
        // Below is a String containing Int Ids from List<int> comma separated
        oneStopDisruptionsIdsList = this.oneStopDisruptionsIdsList,
        routeType = this.routeType,
        directionName = this.directionName,
        destinationName = this.destinationName,
        lineNumber = this.lineNumber,
        lineShortName = this.lineShortName,
        runType = this.runType,
        status = this.status,
        isRealTimeInfo = this.isRealTimeInfo,
        showInMagnifiedView = !this.showInMagnifiedView,
        rowInsertTime = this.rowInsertTime
    )
}

/**
 * Use in debugging/testing - using DeparturesRepositoryFake. It will set unique row id.
 */
fun DatabaseDeparture.setId(newId: Int): DatabaseDeparture {
    return DatabaseDeparture(
        id = newId,
        stopId = this.stopId,
        routeId = this.routeId,
        runId = this.runId,
        departureTimeUtc = this.departureTimeUtc,
        departureDayMonthNoLeadingZeros = this.departureDayMonthNoLeadingZeros,
        departureTimeHourMinutes = this.departureTimeHourMinutes,
        displayDepartureDtTm = this.displayDepartureDtTm,
        atPlatform = this.atPlatform,
        platformNumber = this.platformNumber,
        // Below is a String containing Int Ids from List<int> comma separated
        oneStopDisruptionsIdsList = this.oneStopDisruptionsIdsList,
        routeType = this.routeType,
        directionName = this.directionName,
        destinationName = this.destinationName,
        lineNumber = this.lineNumber,
        lineShortName = this.lineShortName,
        runType = this.runType,
        status = this.status,
        isRealTimeInfo = this.isRealTimeInfo,
        showInMagnifiedView = this.showInMagnifiedView,
        rowInsertTime = this.rowInsertTime
    )
}

/*
    Below are not used anywhere in the Production version of the adapter

    val directionId = Int,
    val suburb: String?,
    val numSkipped: Int,
    val destinationId: Int,
 */