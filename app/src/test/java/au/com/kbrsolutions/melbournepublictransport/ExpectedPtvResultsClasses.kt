package au.com.kbrsolutions.melbournepublictransport

import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.*

object Responses {
    private val disruptionsIntList = listOf(182985, 182209, 181613, 175928)
    private val departures = listOf(
        Departure(stopId=1035, routeId=6, runId=15293, directionId=5, disruptionIds=disruptionsIntList, scheduledDepartureUtc="2019-08-26T22:49:00Z", estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags="S_WCA-S_RRB-RUN_67", departureSequence=0),
        Departure(stopId=1035, routeId=6, runId=15694, directionId=1, disruptionIds=disruptionsIntList, scheduledDepartureUtc="2019-08-26T22:50:00Z", estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags="S_WCA-S_RRB-RUN_101", departureSequence=0),
        Departure(stopId=1035, routeId=6, runId=15294, directionId=5, disruptionIds=disruptionsIntList, scheduledDepartureUtc="2019-08-26T22:57:00Z", estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags="S_WCA-S_RRB-RUN_68", departureSequence=0),
        Departure(stopId=1035, routeId=6, runId=15697, directionId=1, disruptionIds=disruptionsIntList, scheduledDepartureUtc="2019-08-26T22:58:00Z", estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags="S_WCA-S_RRB-RUN_104", departureSequence=0),
        Departure(stopId=1035, routeId=6, runId=15295, directionId=5, disruptionIds=disruptionsIntList, scheduledDepartureUtc="2019-08-26T23:05:00Z", estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags="S_WCA-S_RRB-RUN_69", departureSequence=0)
    )
    private val stops = java.util.HashMap<String, Stop>()
    private val routes =  mapOf<String, Route>()
    private val runs = java.util.HashMap<String, Run>()
    private val directions = mapOf<String, Direction>()
    private val disruptions = java.util.HashMap<String, Disruption>()
    private val status = Status("3.0", 1)
    /*
        status={3.0=1.0})
        status={version=3.0, health=1.0})
     */

    public val expectedDeparturesClass = DeparturesObjectsFromJson(departures,  stops, routes, runs, directions, disruptions, status)
}

/*
createClassFromJson - result: DeparturesObjectsFromJson(departures=[
    DatabaseDeparture(stopId=1035, routeId=6, runId=15293, directionId=5, disruptionIds=[182985, 182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T22:49:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_67, departureSequence=0),
    DatabaseDeparture(stopId=1035, routeId=6, runId=15694, directionId=1, disruptionIds=[182985, 182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T22:50:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_101, departureSequence=0),
    DatabaseDeparture(stopId=1035, routeId=6, runId=15294, directionId=5, disruptionIds=[182985, 182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T22:57:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_68, departureSequence=0),
    DatabaseDeparture(stopId=1035, routeId=6, runId=15697, directionId=1, disruptionIds=[182985, 182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T22:58:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_104, departureSequence=0),
    DatabaseDeparture(stopId=1035, routeId=6, runId=15295, directionId=5, disruptionIds=[182985, 182209, 181613, 175928], scheduledDepartureUtc=2019-08-26T23:05:00Z, estimatedDepartureUtc=null, atPlatform=false, platformNumber=null, flags=S_WCA-S_RRB-RUN_69, departureSequence=0)],
stops={}, routes={}, runs={}, directions={}, disruptions={}, status={version=3.0, health=1.0}) */
