package au.com.kbrsolutions.melbournepublictransport.departures

import au.com.kbrsolutions.melbournepublictransport.network.PtvServiceUtils

object DynamicUrlBuilder {

    private const val EXPAND = "expand="
    private const val AMPERSAND = "&"
    private const val DEPARTURE = "/departures/route_type/"
    private const val STOP = "/stop/"
    private const val MAX_RESULTS = "?max_results="
    private const val INCLUDE_CANCELLED_TEXT = "&include_cancelled="
    private const val TRUE = "true"

    fun buildURI(routeType: Int, stopId: String, limit: Int, expandElements: Set<String>)
            : String {
        val expandOptions = StringBuilder("")
        var notFstTime = false
        for (expandElement in expandElements) {
            if (notFstTime) {
                expandOptions.append(AMPERSAND)
            }
            expandOptions.append(EXPAND).append(expandElement)
            notFstTime = true
        }
        val dynamicUrl = "" + DEPARTURE +
                routeType +
                STOP +
                stopId +
                MAX_RESULTS +
                limit +
                INCLUDE_CANCELLED_TEXT +
                TRUE +
                if (expandOptions.isNotEmpty()) ("""$AMPERSAND$expandOptions""") else ""

        return PtvServiceUtils.buildTTAPIURL(dynamicUrl)
    }
}