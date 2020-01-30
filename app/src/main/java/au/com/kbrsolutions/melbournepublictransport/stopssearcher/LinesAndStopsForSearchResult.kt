package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import au.com.kbrsolutions.melbournepublictransport.data.DatabaseLineStopDetails

class LinesAndStopsForSearchResult(
    val health: Boolean,
    var lineStopDetailsList: List<DatabaseLineStopDetails>
)