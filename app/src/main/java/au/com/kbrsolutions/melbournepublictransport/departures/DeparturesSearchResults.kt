package au.com.kbrsolutions.melbournepublictransport.departures

import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture

class DeparturesSearchResults(
    val health: Boolean,
    var departuresDetailsList: List<DatabaseDeparture>)