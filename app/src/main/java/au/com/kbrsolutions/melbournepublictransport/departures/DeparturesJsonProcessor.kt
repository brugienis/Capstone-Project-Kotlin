package au.com.kbrsolutions.melbournepublictransport.departures

import android.content.Context
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.*
import au.com.kbrsolutions.melbournepublictransport.utilities.JodaDateTimeUtility
import org.joda.time.format.DateTimeFormat

object DeparturesJsonProcessor {

    fun buildDepartureDetailsList(departuresObjectsFromJson: DeparturesObjectsFromJson,
                                  context: Context): DeparturesSearchResults {

        val fmtHHmm = DateTimeFormat.forPattern("HH:mm")
        val fmtdM = DateTimeFormat.forPattern("d-M")
        val fmtdMddd = DateTimeFormat.forPattern("d MMM (EEE)")

        val detailsList = mutableListOf<DatabaseDeparture>()

//        val stops: Map<String, Stop>? = departuresObjectsFromJson.stops
        val routes: Map<String, Route>? = departuresObjectsFromJson.routes
        val directions: Map<String, Direction>? = departuresObjectsFromJson.directions
        val runs: Map<String, Run>? = departuresObjectsFromJson.runs
        val status: Status = departuresObjectsFromJson.status

        departuresObjectsFromJson.departures.forEach {
            var expressStopCount = 0
            var destinationName: String? = null
            if (runs != null) {
                expressStopCount = runs[it.routeId.toString()]?.expressStopCount ?: 0
                destinationName = runs[it.runId.toString()]?.destinationName
            }
            val runType = if (expressStopCount > 0) {
                context.getString(R.string.express_train, destinationName)
            } else  {
                context.getString(R.string.all_stops_train, destinationName)
            }
            var lineNumber: String? = null
            var lineShortName: String? = null
            if (routes != null) {
                lineNumber = routes[it.routeId.toString()]?.routeNumber
                lineShortName = routes[it.routeId.toString()]?.routeName
            }
            val scheduledDepartureUtc = it.scheduledDepartureUtc
            val estimatedDepartureUtc = it.estimatedDepartureUtc

            var departureTimeUtc: String
            var isRealTimeInfo = true
            if (estimatedDepartureUtc == null) {
                isRealTimeInfo = false
                departureTimeUtc = scheduledDepartureUtc!!
            } else {
                departureTimeUtc = estimatedDepartureUtc
            }

            val departureDayMonthNoLeadingZeros =
                fmtdM.print(JodaDateTimeUtility.getLocalTimeFromUtcString(departureTimeUtc))
            val displayDepartureDtTm =
                fmtdMddd.print(JodaDateTimeUtility.getLocalTimeFromUtcString(departureTimeUtc))
            val departureTimeHourMinutes =
                fmtHHmm.print(JodaDateTimeUtility.getLocalTimeFromUtcString(departureTimeUtc))

            var platformNumber = it.platformNumber
            if (platformNumber != null) {
                platformNumber = context.getString(R.string.platform_number, it.platformNumber)
            }
            val localIds = it.disruptionIds

            val sb = StringBuffer()
            localIds?.forEach {oneDisruptionId ->
                sb.append("$oneDisruptionId,")
            }
            var disruptionIdsString = ""
            if (sb.isNotEmpty()) {
                sb.setLength(sb.length - 1)
                disruptionIdsString = sb.toString()
            }

            detailsList.add(
                DatabaseDeparture(
                    0,
                    it.stopId,
                    it.routeId,
                    it.runId,
                    departureTimeUtc,
                    departureDayMonthNoLeadingZeros,
                    departureTimeHourMinutes,
                    displayDepartureDtTm,
                    it.atPlatform,
                    platformNumber,
                    disruptionIdsString,
                    if (routes != null) routes[it.routeId.toString()]?.routeType ?: -1 else -1,
                    if (directions != null) directions[it.directionId.toString()]?.directionName else null, //"directionName",
                    if (runs != null) runs[it.runId.toString()]?.destinationName else null,
                    lineNumber,
                    lineShortName,
                    runType,
//                    status.health,
                    isRealTimeInfo
                )
            )
        }
//        Log.v("DeparturesJsonProcessor", """buildDepartureDetailsList - detailsList: ${detailsList} """)
        return DeparturesSearchResults(status.health == 1, detailsList)
    }
}