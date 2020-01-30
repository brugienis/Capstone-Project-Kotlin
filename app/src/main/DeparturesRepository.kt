package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.domain.Departure

interface DeparturesRepository : Repository {

    val departuresLoadingInProgress: MutableLiveData<Boolean>

    val loadErrMsg: MutableLiveData<String>

    fun getDepartures(favoriteStopsRequestedTimeMillis: Long): LiveData<List<Departure>>

    suspend fun clearTableAndInsertNewRows(databaseDepartures: List<DatabaseDeparture>)

    suspend fun toggleMagnifiedNormalView(id: Int)

    // fixLater: Oct 23, 2019 - it looks like the below is not in use - if confirmed, remove it.
    suspend fun deleteAllDepartures()

    suspend fun refreshPtvData(path: String, context: Context)
}