package au.com.kbrsolutions.melbournepublictransport.repository

//import au.com.kbrsolutions.melbournepublictransport.utilities.USE_HARD_CODED_PVT_RESPONSE
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DepartureDao
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.network.PtvApi
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
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

    override fun getDeparture(id: Int): Departure {
        return departureDao.getDeparture(id).asDomainModel()
    }

    override suspend fun toggleMagnifiedNormalView(id: Int) {
        withContext(Dispatchers.IO) {
            departureDao.toggleMagnifiedNormalView(id)
        }
    }

    override suspend fun deleteAllDepartures() {
        departureDao.deleteAllDepartures()
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
            var departuresObjectsFromJson: DeparturesObjectsFromJson?
            departuresLoadingInProgress.postValue(true)
            Log.v(G_P + "DeparturesRepositoryReal", """refreshPtvData - path: ${path} """)
            try {
                if (SharedPreferencesUtility.useHardCodedPvtResponse(context)) {
                    departuresObjectsFromJson =
                        DebugUtilities(context).getDeparturesResponse(path)
//                if (true) throw RuntimeException("BR test")
                } else {
                    val getDeparturesDeferred: Deferred<DeparturesObjectsFromJson> =
                        PtvApi.retrofitService.getPtvResponse(path)
                    departuresObjectsFromJson = getDeparturesDeferred.await()
                }
                departuresLoadingInProgress.postValue(false)

                var departuresSearchResults = DeparturesJsonProcessor
                    .buildDepartureDetailsList(departuresObjectsFromJson, context)


                if (!departuresSearchResults.health) {
                    throw java.lang.RuntimeException(context.getString(R.string.ptv_is_not_available))
                }

                var databaseDepartureDetails=
                    departuresSearchResults.departuresDetailsList
                departureDao.clearTableAndInsertNewRows(databaseDepartureDetails)
//                _status.value = DeparturesApiStatus.DONE
                departuresLoadingInProgress.postValue(false)
                loadErrMsg.postValue( null)
                Log.v("DeparturesViewModel", """getPtvResponse -  departuresLoadingInProgress.value: ${ departuresLoadingInProgress.value} """)
            } catch (e: Exception) {
//                _status.value = DeparturesApiStatus.ERROR
//                loadErrMsg.value = "test error rmessage"
                loadErrMsg.postValue( "${e.message}")
            }
        }
    }
}

/*
https://timetableapi.ptv.vic.gov.au/v3/departures/route_type/0/stop/Embarcadero?max_results=5&include_cancelled=true&expand=all&expand=stop&expand=route&expand=run&expand=disruption&devid=3000165&signature=E5E0A253C17606D8A642D54E64FDC1ABA428E9DA
https://timetableapi.ptv.vic.gov.au/v3/departures/route_type/0/stop/1012?devid=3000165&signature=5980C641BCDD749DA96AF890E8CAE648CA22B6D8
 */