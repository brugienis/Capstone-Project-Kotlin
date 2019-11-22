package au.com.kbrsolutions.melbournepublictransport.utilities

import au.com.kbrsolutions.melbournepublictransport.domain.Departure

object Misc {

    /**
     *
     * Sort the data by 'databaseDeparture time' or 'direction name and 'databaseDeparture time'.
     *
     * Based on https://www.baeldung.com/kotlin-sort
     *
     * @param sortByTime    true, if data should be sorted by time.
     */
    fun sortDeparturesData(departures: List<Departure>, sortByTime: Boolean)
            : List<Departure> {
        return when (sortByTime) {
            true -> {
                departures.sortedWith(compareBy({ it.departureTimeHourMinutes}))
            }
            else -> {
                departures.sortedWith(compareBy({ it.directionName}, { it.departureTimeHourMinutes }))
            }
        }
    }
}