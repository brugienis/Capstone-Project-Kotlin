package au.com.kbrsolutions.melbournepublictransport.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.data.flipShowInMagnifiedView
import au.com.kbrsolutions.melbournepublictransport.data.setId
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.network.PtvApi
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import kotlinx.coroutines.*
import java.util.*

class DeparturesRepositoryFake : DeparturesRepository {

    /* map 'id' to DatabaseDeparture */
    var departuresServiceData: LinkedHashMap<Int, DatabaseDeparture> = LinkedHashMap()

    private var _departures = MutableLiveData<List<Departure>>()

    override val departuresLoadingInProgress = MutableLiveData<Boolean>()

    override val loadErrMsg = MutableLiveData<String>()

    private var delayMillis = 0L

    private var debuggingJsonStringFile = "departures_results_all_expand_options.json"
//    private var debuggingJsonStringFile = "departures_results_sample.json"
//    private var debuggingJsonStringFile = "departures_results_sample_health_false.json"

    fun setSimulatedDelayMillis(delayMillis: Long) {
        this.delayMillis = delayMillis
    }

    override fun getDepartures(favoriteStopsRequestedTimeMillis: Long):
            LiveData<List<Departure>> = _departures

    override fun getDeparture(id: Int): Departure {
        return departuresServiceData[id]!!.asDomainModel()
    }

    // fixLater: Jan 28, 2020 - where is the code to clear departuresServiceData?
    private suspend fun clearTableAndInsertNewRows(databaseDepartures: List<DatabaseDeparture>) {
        withContext(Dispatchers.IO) {
            var oneDatabaseDeparture: DatabaseDeparture
            databaseDepartures.forEachIndexed { index, databaseDeparture ->
                oneDatabaseDeparture = databaseDeparture.setId(index)
                departuresServiceData[oneDatabaseDeparture.id] = oneDatabaseDeparture
//                if (index < 10) {
//                    println(GLOBAL_PREFIX + oneDatabaseDeparture)
//                }
            }
            updateLiveData("clearTableAndInsertNewRows")
        }
    }

    override suspend fun toggleMagnifiedNormalView(id: Int) {
        Log.v(G_P + "DeparturesRepositoryFake", """toggleMagnifiedNormalView - id: ${id} """)
        runBlocking {
            val magnifiedDepartureList = departuresServiceData.values.filter {
                it.showInMagnifiedView == true
            }
            val listSize = magnifiedDepartureList.size
            if (listSize > 1) {
                throw RuntimeException("""BR - 
                    |DeparturesRepositoryFake.toggleMagnifiedNormalView(): 
                    |more then one magnified row""".trimMargin())
            } else if (listSize == 1) {
                flipShowMagnifyView(magnifiedDepartureList[0].id)
            }
            if (listSize == 0 || magnifiedDepartureList[0].id != id) {
                flipShowMagnifyView(id)
            }
        }
    }

    private fun flipShowMagnifyView(id: Int) {
        runBlocking {
            val departure: DatabaseDeparture = departuresServiceData[id]!!
            departuresServiceData[id] = departure.flipShowInMagnifiedView()
            updateLiveData("flipShowMagnifyView")
        }
    }

    override suspend fun deleteAllDepartures() {
        runBlocking {
            delay(delayMillis)
            departuresServiceData.clear()
            updateLiveData("deleteAllDepartures")
        }
    }

    @SuppressLint("LongLogTag")
    override suspend fun refreshPtvData(path: String, context: Context) {
        Log.v("DeparturesRepositoryFake", """refreshPtvData - path: ${path} """)

        EspressoIdlingResource.increment("DeparturesRepositoryFake.refreshPtvData") // Set app as busy.
        departuresLoadingInProgress.postValue(true)
        withContext(Dispatchers.IO) {
            //            _status.value = DeparturesApiStatus.LOADING
            var departuresObjectsFromJson: DeparturesObjectsFromJson?
            departuresLoadingInProgress.postValue(true)
            try {
                if (SharedPreferencesUtility.useHardCodedPvtResponse(context)) {
                    departuresObjectsFromJson =
                        DebugUtilities(context).getDeparturesResponse(debuggingJsonStringFile)
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
                clearTableAndInsertNewRows(databaseDepartureDetails)
//                _status.value = DeparturesApiStatus.DONE
                departuresLoadingInProgress.postValue(false)
                loadErrMsg.postValue( null)
            } catch (e: Exception) {
//                _status.value = DeparturesApiStatus.ERROR
//                loadErrMsg.value = "test error rmessage"
                loadErrMsg.postValue( "${e.message}")
            }
            EspressoIdlingResource.decrement("DeparturesRepositoryFake.refreshPtvData") // Set app as idle.x
        }
    }

    @VisibleForTesting
    fun setDebuggingJsonStringFile(debuggingJsonStringFile: String) {
        this.debuggingJsonStringFile = debuggingJsonStringFile
//        println(G_P + """StopsSearcherRepositoryFake - setDebuggingJsonStringFile - debuggingJsonStringFile: ${debuggingJsonStringFile} """)
    }

    @VisibleForTesting
    fun addDepartures(departures: List<DatabaseDeparture>) {
        for (departure in departures) {
            departuresServiceData[departure.id] = departure
        }
        updateLiveData("addDepartures")
    }

    private fun updateLiveData(source: String) {
        val departuresList = mutableListOf<Departure>()
        departuresServiceData.keys.forEach {
            departuresList.add(departuresServiceData[it]!!.asDomainModel())
        }
        _departures.postValue(departuresList)
//        println(G_P + """DeparturesRepositoryFake - updateLiveData (source: $source) - departuresList size: ${departuresList.size} """)
    }
}