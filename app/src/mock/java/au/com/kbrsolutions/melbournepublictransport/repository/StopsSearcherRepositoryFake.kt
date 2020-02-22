package au.com.kbrsolutions.melbournepublictransport.repository

//import au.com.kbrsolutions.melbournepublictransport.utilities.USE_HARD_CODED_PVT_RESPONSE
import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseLineStopDetails
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.data.flipShowInMagnifiedView
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.network.PtvApi
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.LinesAndStopsForSearchResult
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.jsondata.StopsSearcherObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * This class is used only in the 'mock' variant. It is returned by the ServiceLocator that is in
 * the 'mock' path.
 */
class StopsSearcherRepositoryFake() : StopsSearcherRepository {

    private var debuggingJsonStringFile = "stops_searcher_all_routes.json"

    /* map 'id' to LineStopDetails */
    var stopsSearcherServiceData: LinkedHashMap<Int, DatabaseLineStopDetails> = LinkedHashMap()

    private var _stopsSearcherResults = MutableLiveData<List<LineStopDetails>>()

    override fun getStopsSearcherResults(): LiveData<List<LineStopDetails>> = _stopsSearcherResults

    override val loadErrMsg = MutableLiveData<String>()

    private var delayMillis = 0L

    fun setSimulatedDelayMillis(delayMillis: Long) {
        this.delayMillis = delayMillis
    }
    override suspend fun clearTable() {
        stopsSearcherServiceData.clear()
        updateLiveData()
    }

    override suspend fun getLineStopDetails(id: Int): LineStopDetails {
        return stopsSearcherServiceData[id]!!.asDomainModel()
    }

    override suspend fun sendRequestAndProcessPtvResponse(
        path: String,
        favoriteStopIdsSet: Set<Int>,
        context: Context) {
//        Log.v(G_P + "StopsSearcherRepositoryFake", """sendRequestAndProcessPtvResponse - path: ${path} """)

//        EspressoIdlingResource.increment("StopsSearcherRepositoryFake.sendRequestAndProcessPtvResponse") // Set app as busy.
            /*runBlocking {
                delay(delayMillis)*/

            var stopsSearcherObjectsFromJson: StopsSearcherObjectsFromJson? = null
            try {
                if (SharedPreferencesUtility.useHardCodedPvtResponse(context)) {
                    stopsSearcherObjectsFromJson =
                        DebugUtilities(context).getStopsSearcherResponse(
                            debuggingJsonStringFile)
                } else {
                    val getDeparturesDeferred: Deferred<StopsSearcherObjectsFromJson> =
                        PtvApi.retrofitService.getPtvStopsSearcherResponse(path)
                    stopsSearcherObjectsFromJson = getDeparturesDeferred.await()
                }

                // fixLater: Jan 06, 2020 - the 'stopId' is an Int - why do we pass set of strings?
//                val favoriteStopIdsSetTemp = mutableSetOf<Int>()
                val linesAndStopsForSearchResult: LinesAndStopsForSearchResult =
                    StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
                        stopsSearcherObjectsFromJson,
                        favoriteStopIdsSet)

                if (!linesAndStopsForSearchResult.health) {
                    throw java.lang.RuntimeException(context.getString(R.string.ptv_is_not_available))
                }
                val linesStopsDetails =
                    linesAndStopsForSearchResult.lineStopDetailsList
                loadErrMsg.postValue( null)

                /*linesStopsDetails.forEachIndexed { index, lineStopDetails ->
                    if (index < 10) {
                        Log.v(G_P + "StopsSearcherRepositoryFake", """sendRequestAndProcessPtvResponse - lineStopDetails: ${lineStopDetails} """)
                        println(G_P + """StopsSearcherRepositoryFake - sendRequestAndProcessPtvResponse - lineStopDetails: ${lineStopDetails} """)
                    }
                }*/

                linesStopsDetails.forEach { lineStopDetails ->
                    stopsSearcherServiceData[lineStopDetails.id] = lineStopDetails
                }
                updateLiveData()
            } catch (e: Exception) {
                loadErrMsg.postValue( "${e.message}")
            }
//        EspressoIdlingResource.decrement("StopsSearcherRepositoryFake.sendRequestAndProcessPtvResponse") // Set app as idle.
    }

    /*
        Toggle view layout - normal/magnified.
    */
    override suspend fun toggleMagnifiedView(id: Int) {
        runBlocking {
            val magnifiedFavoriteStopList = stopsSearcherServiceData.values.filter {
                it.showInMagnifiedView == true
            }
            val listSize = magnifiedFavoriteStopList.size
            if (listSize > 1) {
                throw RuntimeException("""BR - 
                    |StopsSearcherRepositoryFake.toggleMagnifiedNormalView(): 
                    |more then one magnified row""".trimMargin())
            } else if (listSize == 1) {
                flipShowMagnifyView(magnifiedFavoriteStopList[0].id)
            }
            if (listSize == 0 || magnifiedFavoriteStopList[0].id != id) {
                flipShowMagnifyView(id)
            }
        }
    }

    private fun flipShowMagnifyView(id: Int) {
        runBlocking {
            val lineStopDetails: DatabaseLineStopDetails = stopsSearcherServiceData[id]!!
            printStopDetails(stopsSearcherServiceData[id]!!, "before")
            stopsSearcherServiceData[id] = lineStopDetails.flipShowInMagnifiedView()
            updateLiveData()
        }
    }

    override suspend fun getLineStopDetailsCount(): Int {
        return stopsSearcherServiceData.size
    }

    @VisibleForTesting
    fun setDebuggingJsonStringFile(debuggingJsonStringFile: String) {
        this.debuggingJsonStringFile = debuggingJsonStringFile
//        println(G_P + """StopsSearcherRepositoryFake - setDebuggingJsonStringFile - debuggingJsonStringFile: ${debuggingJsonStringFile} """)
    }

    private fun printStopDetails(lineStopDetails: DatabaseLineStopDetails, source: String, msg: String = "") {
        println("StopsSearcherRepositoryFake - printStopDetails - source - $msg stopId: ${lineStopDetails.id} stopName: ${lineStopDetails.lineId} magnified: ${lineStopDetails.showInMagnifiedView}")
    }

    private fun updateLiveData() {
        val stopsSearcherResultsList = mutableListOf<LineStopDetails>()
        stopsSearcherServiceData.keys.forEach {
            stopsSearcherResultsList.add(stopsSearcherServiceData[it]!!.asDomainModel())
        }
        _stopsSearcherResults.postValue(stopsSearcherResultsList)
//        Log.v(G_P + "StopsSearcherRepositoryFake", """updateLiveData - stopsSearcherResultsList: ${stopsSearcherResultsList.size} """)
    }
}

