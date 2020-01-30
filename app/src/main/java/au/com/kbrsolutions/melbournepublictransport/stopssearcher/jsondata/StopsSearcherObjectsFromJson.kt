package au.com.kbrsolutions.melbournepublictransport.stopssearcher.jsondata

import com.squareup.moshi.Json

/*
    Generated from Json string using
        http://www.jsonschema2pojo.org/

    See notes of the project in Evernote - Timeline.
 */

data class StopsSearcherObjectsFromJson(
    val stops: List<StopsSearcherStop>,
    val routes: List<StopsSearcherRoute>,
    val outlets: List<Outlet>,
    val status: Status)

data class StopsSearcherStop(
    @Json(name = "stop_distance")
    val stopDistance: Double,
    @Json(name = "stop_suburb")
    val stopSuburb: String,
    @Json(name = "stop_name")
    val stopName: String,
    @Json(name = "stop_id")
    val stopId: Int,
    @Json(name = "route_type")
    val routeType: Int,
    @Json(name = "stop_latitude")
    val stopLatitude: Double,
    @Json(name = "stop_longitude")
    val stopLongitude: Double,
    @Json(name = "stop_sequence")
    val stopSequence: Int)

data class StopsSearcherRoute(
    @Json(name = "route_name")
    val routeName: String?,
    @Json(name = "route_number")
    val routeNumber: String?,
    @Json(name = "route_type")
    val routeType: Int?,
    @Json(name = "route_id")
    val routeId: Int?,
    @Json(name = "route_gtfs_id")
    val routeGtfsId: String?,
    @Json(name = "route_service_status")
    val routeServiceStatus: RouteServiceStatus?)

data class RouteServiceStatus(
    @Json(name = "description")
    var description: String?,
    @Json(name = "timestamp")
    var timestamp: String?)

data class Outlet(
    @Json(name = "outlet_distance")
    val outletDistance: Int?,
    @Json(name = "outlet_slid_spid")
    val outletSlidSpid: String?,
    @Json(name = "outlet_name")
    val outletName: String?,
    @Json(name = "outlet_business")
    val outletBusiness: String?,
    @Json(name = "outlet_latitude")
    val outletLatitude: Double?,
    @Json(name = "outlet_longitude")
    val outletLongitude: Double?,
    @Json(name = "outlet_suburb")
    val outletSuburb: String?,
    @Json(name = "outlet_postcode")
    val outletPostcode: Int?,
    @Json(name = "outlet_business_hour_mon")
    val outletBusinessHourMon: Any?,
    @Json(name = "outlet_business_hour_tue")
    val outletBusinessHourTue: Any?,
    @Json(name = "outlet_business_hour_wed")
    val outletBusinessHourWed: Any?,
    @Json(name = "outlet_business_hour_thur")
    val outletBusinessHourThur: Any?,
    @Json(name = "outlet_business_hour_fri")
    val outletBusinessHourFri: Any?,
    @Json(name = "outlet_business_hour_sat")
    val outletBusinessHourSat: Any?,
    @Json(name = "outlet_business_hour_sun")
    val outletBusinessHourSun: Any?,
    @Json(name = "outlet_notes")
    val outletNotes: Any?)

data class Status(val version: String, val health: Int)
