package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import au.com.kbrsolutions.melbournepublictransport.data.DatabaseLineStopDetails
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.jsondata.StopsSearcherObjectsFromJson
import java.util.*
import kotlin.Comparator

object StopsSearcherJsonProcessor {

    fun buildStopsSearcherDetailsList(
        stopsSearcherObjectsFromJson: StopsSearcherObjectsFromJson, favoriteStopIdsSet: Set<Int>)
            : LinesAndStopsForSearchResult {
        var lineStopDetailsId = 1   // start from 1. If start is 0, the Room DB will change 'id' to 1
        var routeType: Int
        var lineOrStopType: Int
        var lineId: Int
        var routeId: Int
        var stopId: Int
        var stopLocationOrLineName: String
        var latitude: Double
        var longitude: Double
        var distance: Double
        val lineStopDetailsList = mutableListOf<DatabaseLineStopDetails>()
        val lineStopDetailsSet: MutableSet<DatabaseLineStopDetails> =
            TreeSet(Comparator { o1, o2 ->
                // Sort by routeId - train, tram, bus, night rider
                if (o1.routeType !== o2.routeType) {
                    if (o1.routeType < o2.routeType) {
                        -1
                    } else {
                        1
                    }
                } else { // sort by stop or line name
                    o1.stopLocationOrLineName.compareTo(o2.stopLocationOrLineName)
                }
            })

        val status = stopsSearcherObjectsFromJson.status

        /* Processes 'stops' */
        lineId = -1
        stopsSearcherObjectsFromJson.stops.forEach { stop ->
            stopLocationOrLineName = stop.stopName
            routeType = stop.routeType
            lineOrStopType = LineStopDetails.STOP_DETAILS
            stopId = stop.stopId
            latitude = stop.stopLatitude
            longitude = stop.stopLongitude
            distance = stop.stopDistance
            if (!favoriteStopIdsSet.contains(stopId)) {
                lineStopDetailsSet.add(
                    DatabaseLineStopDetails(
                        lineStopDetailsId++,  // id,
                        routeType,
                        lineOrStopType,
                        lineId,
                        stopId,
                        stopLocationOrLineName,
                        stop.stopSuburb,
                        latitude,
                        longitude,
                        distance,
                        null,  // favorite
                        null,  // lineNameShort as 'suburb' above
                        null, // lineNumber as 'suburb' above
                        false
                    )
                )
            }
        }

        /* Processes 'lines' */
        latitude = -1.0
        longitude = -1.0
        stopsSearcherObjectsFromJson.routes.forEach { route ->
            lineId = -1
            distance = -1.0
            stopLocationOrLineName = route.routeName ?: ""
            routeType = route.routeType ?: -1
            lineOrStopType = LineStopDetails.LINE_DETAILS

            val routeId = route.routeId ?: -1
            stopId = routeId

            if (!favoriteStopIdsSet.contains(stopId)) {
                lineStopDetailsSet.add(
                    DatabaseLineStopDetails(
                        lineStopDetailsId++,  // id,
                        routeType,
                        lineOrStopType,
                        routeId,
                        stopId,
                        stopLocationOrLineName,
                        null,  // suburb,
                        latitude,
                        longitude,
                        distance,
                        null,  // favorite
                        null,  // lineNameShort,
                        null, //lineNumber, // lineNumber as 'suburb' above
                        false
                    )
                )
            }
        }

        lineStopDetailsList.addAll(lineStopDetailsSet)
        return  LinesAndStopsForSearchResult(status.health == 1, lineStopDetailsList)
    }
}