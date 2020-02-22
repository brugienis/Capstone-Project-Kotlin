package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import au.com.kbrsolutions.melbournepublictransport.common.LatLngDetails
import au.com.kbrsolutions.melbournepublictransport.network.PtvServiceUtils
import au.com.kbrsolutions.melbournepublictransport.utilities.Miscellaneous

object StopsSearcherUrlBuilder {

    // https://jsonformatter.org/


    private const val SEARCH = "/search/"
    private const val ROUTE_TYPES = "route_types="
    private const val LATITUDE_TEXT = "latitude="
    private const val LONGITUDE_TEXT = "longitude="
    private const val MAX_DISTANCE_TEXT = "max_distance="
    private const val QUESTION_MARK = "?"
    private const val AMPERSAND = "&"

    fun buildURI(
        searchText: String,
        latLngDetails: LatLngDetails?,
        searchMaxDistanceMeters: Float,
        routeTypes: Set<String>?): String {
        val sb = StringBuilder().apply {
            append(SEARCH)
            append(
                Miscellaneous.replaceSearchTextMultipleSpacesWithOne(
                    searchText.replace("\n".toRegex(), "")
                )
            )
            append(QUESTION_MARK)
            var isFstRouteTypeProcessed = false
            routeTypes?.let {
                for (routeType in routeTypes) {
                    if (isFstRouteTypeProcessed) {
                        append(AMPERSAND)
                    } else {
                        isFstRouteTypeProcessed = true
                    }
                    append(ROUTE_TYPES).append(routeType)
                }
            }
            latLngDetails?.let {
                if (routeTypes != null) {
                    append(AMPERSAND)
                }
                append(LATITUDE_TEXT)
                append(latLngDetails.latitude)
                append(AMPERSAND)
                append(LONGITUDE_TEXT)
                append(latLngDetails.longitude)
            }
            if (searchMaxDistanceMeters > 0) {
                append(AMPERSAND)
                append(MAX_DISTANCE_TEXT)
                append(searchMaxDistanceMeters)
            }
        }
        // /v3/search/frankst?devid
        // /v3Frankst?route_types=0&rou

        return PtvServiceUtils.buildTTAPIURL(sb.toString())
    }
}