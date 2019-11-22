package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.data.DepartureDao
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.network.PtvApi
import au.com.kbrsolutions.melbournepublictransport.utilities.USE_HARD_CODED_PVT_RESPONSE
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeparturesRepositoryReal(private val departureDao: DepartureDao)
    : DeparturesRepository  {

    override val departuresLoadingInProgress = MutableLiveData<Boolean>()

    override val loadErrMsg = MutableLiveData<String>()

    override fun getDepartures(favoriteStopsRequestedTimeMillis: Long): LiveData<List<Departure>> =
        Transformations.map(departureDao.getDepartures(favoriteStopsRequestedTimeMillis)) {
            it.asDomainModel()
        }

    override suspend fun clearTableAndInsertNewRows(databaseDepartures: List<DatabaseDeparture>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun toggleMagnifiedNormalView(id: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteAllDepartures() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Refresh the Ptv data stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     */
    override suspend fun refreshPtvData(path: String, context: Context) {
        withContext(Dispatchers.IO) {

            //            _status.value = DeparturesApiStatus.LOADING
            var departuresObjectsFromJson: DeparturesObjectsFromJson? = null
            departuresLoadingInProgress.postValue(true)
            try {
                if (USE_HARD_CODED_PVT_RESPONSE) {
                    departuresObjectsFromJson =
                        DebugUtilities(context).getDeparturesResponse()
//                if (true) throw RuntimeException("BR test")
                } else {
                    val getDeparturesDeferred: Deferred<DeparturesObjectsFromJson> =
                        PtvApi.retrofitService.getPtvResponse(path)
                    departuresObjectsFromJson = getDeparturesDeferred.await()
                }
                departuresLoadingInProgress.postValue(false)

                var databaseDepartureDetails: List<DatabaseDeparture> = DeparturesJsonProcessor
                    .buildDepartureDetailsList(departuresObjectsFromJson, context)
                departureDao.clearTableAndInsertNewRows(databaseDepartureDetails)
//                _status.value = DeparturesApiStatus.DONE
                departuresLoadingInProgress.postValue(false)
                Log.v("DeparturesViewModel", """getPtvResponse -  departuresLoadingInProgress.value: ${ departuresLoadingInProgress.value} """)
            } catch (e: Exception) {
//                _status.value = DeparturesApiStatus.ERROR
//                loadErrMsg.value = "test error rmessage"
                loadErrMsg.postValue( "Error message: $e")
            }
        }
    }
}