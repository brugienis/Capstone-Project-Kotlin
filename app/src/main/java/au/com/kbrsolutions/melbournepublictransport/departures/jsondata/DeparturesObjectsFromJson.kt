package au.com.kbrsolutions.melbournepublictransport.departures.jsondata

import com.squareup.moshi.Json

/*
    Generated from Json string using
        http://www.jsonschema2pojo.org/

    See notes of the project in Evernote - Timeline.
 */
data class DeparturesObjectsFromJson(
    val departures: List<Departure>,
    val stops: Map<String, Stop>,
    val routes: Map<String, Route>?,
    val runs: Map<String, Run>,
    val directions: Map<String, Direction>,
    val disruptions: Map<String, Disruption>,
    val status: Status)

data class Departure (
    @Json(name = "stop_id")
    var stopId: Int?,
    @Json(name = "route_id")
    var routeId: Int?,
    @Json(name = "run_id")
    var runId: Int?,
    @Json(name = "direction_id")
    var directionId: Int?,
    @Json(name = "disruption_ids")
    var disruptionIds: List<Int>?,
    @Json(name = "scheduled_departure_utc")
    var scheduledDepartureUtc: String?,
    @Json(name = "estimated_departure_utc")
    var estimatedDepartureUtc: String?,
    @Json(name = "at_platform")
    var atPlatform: Boolean?,
    @Json(name = "platform_number")
    var platformNumber: String?,
    @Json(name = "flags")
    var flags: String?,
    @Json(name = "departure_sequence")
    var departureSequence: Int?)

data class Stop(
    @Json(name = "stop_id")
    val stopId: Integer,
    @Json(name = "stop_distance")
    val stopDistance: Double,
    @Json(name = "stop_suburb")
    val stopSuburb: String,
    @Json(name = "stop_name")
    val stopName: String,
    @Json(name = "route_type")
    val routeType: Integer,
    @Json(name = "stop_latitude")
    val stopLatitude: Double,
    @Json(name = "stop_longitude")
    val stopLongitude: Double,
    @Json(name = "stop_sequence")
    val stopSequence: Integer
    )

class Route (
    @Json(name = "route_id")
    val routeId: Int?,
    @Json(name = "route_type")
    val routeType: Int?,
    @Json(name = "route_name")
    val routeName: String?,
    @Json(name = "route_number")
    val routeNumber: String?,
    @Json(name = "route_gtfs_id")
    val routeGtfsId: String?)

data class Direction(
    @Json(name = "direction_id")
    val directionId: Int?,
    @Json(name = "direction_name")
    val directionName: String?,
    @Json(name = "route_id")
    val routeId: Int?,
    @Json(name = "route_type")
    val routeType: Int?)

data class Run(
    @Json(name = "run_id")
    val runId: String,
    @Json(name = "destination_name")
    val destinationName: String,
    @Json(name = "express_stop_count")
    val expressStopCount: Int
)

data class Disruption(
    @Json(name = "disruption_id")
    val disruptionId: Int?,
    @Json(name = "title")
    val title: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "disruption_status")
    val disruptionStatus: String?,
    @Json(name = "disruption_type")
    val disruptionType: String?,
    @Json(name = "published_on")
    val publishedOn: String?,
    @Json(name = "last_updated")
    val lastUpdated: String?,
    @Json(name = "from_date")
    val fromDate: String?,
    @Json(name = "to_date")
    val toDate: String?,
    @Json(name = "routes")
    val routes: List<Route>?,
    @Json(name = "stops")
    val stops: List<Any>?,
    @Json(name = "colour")
    val colour: String?,
    @Json(name = "display_on_board")
    val displayOnBoard: Boolean?,
    @Json(name = "display_status")
    val displayStatus: Boolean?)

data class Status(val version: String, val health: Int)

/*
    DeparturesObjectsFromJson(
        departures=[
            DatabaseDeparture(stopId=1035, routeId=6, runId=15465, directionId=5, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:14:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_239, departureSequence=0),
            DatabaseDeparture(stopId=1035, routeId=6, runId=15852, directionId=1, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:15:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_259, departureSequence=0),
            DatabaseDeparture(stopId=1035, routeId=6, runId=15468, directionId=5, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:22:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_242, departureSequence=0),
            DatabaseDeparture(stopId=1035, routeId=6, runId=15854, directionId=1, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:23:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_261, departureSequence=0),
            DatabaseDeparture(stopId=1035, routeId=6, runId=15469, directionId=5, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:30:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_243, departureSequence=0),
            DatabaseDeparture(stopId=1035, routeId=6, runId=15864, directionId=1, disruptionIds=[182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T07:31:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_271, departureSequence=0)
        ],
        stops={},
        routes={},
        runs={},
        directions={},
        disruptions={},
        status={version=3.0, health=1.0})

 */