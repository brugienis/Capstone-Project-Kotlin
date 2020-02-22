package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.domain.Departure

interface DeparturesRepository : Repository {

    val departuresLoadingInProgress: MutableLiveData<Boolean>

    val loadErrMsg: MutableLiveData<String>

    fun getDepartures(favoriteStopsRequestedTimeMillis: Long): LiveData<List<Departure>>

    fun getDeparture(id: Int): Departure

    suspend fun toggleMagnifiedNormalView(id: Int)

    // Below is used in the ServiceLocator.resetDeparturesRepository()
    suspend fun deleteAllDepartures()

    suspend fun refreshPtvData(path: String, context: Context)
}