package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseLineStopDetails
import au.com.kbrsolutions.melbournepublictransport.data.LineStopDetailsDao
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.network.PtvApi
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.LinesAndStopsForSearchResult
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.StopsSearcherJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.jsondata.StopsSearcherObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StopsSearcherRepositoryReal(private val lineStopDetailsDao: LineStopDetailsDao)
    : StopsSearcherRepository {

    override fun getStopsSearcherResults(): LiveData<List<LineStopDetails>> =
        Transformations.map(lineStopDetailsDao.getStopsSearcherResults()) {
            it.asDomainModel()
        }

    override val loadErrMsg = MutableLiveData<String>()

    override val isLoading = MutableLiveData<Boolean>()

    override suspend fun clearTable() {
        withContext(Dispatchers.IO) {
            lineStopDetailsDao.clear()
        }
    }

    override suspend fun getLineStopDetails(id: Int): LineStopDetails {
        var lineStopDetails: DatabaseLineStopDetails? = null
        withContext(Dispatchers.IO) {
            lineStopDetails = lineStopDetailsDao.getLineStopDetails(id)
        }
        return lineStopDetails!!.asDomainModel()
    }

    override suspend fun sendRequestAndProcessPtvResponse(
        path: String,
        favoriteStopIdsSet: Set<Int>,
        context: Context) {
        withContext(Dispatchers.IO) {
            isLoading.postValue(true)
            val useHardCodedPvtResponse = SharedPreferencesUtility.useHardCodedPvtResponse(context)
            Log.v(
                G_P + "StopsSearcherRepositoryReal",
                """sendRequestAndProcessPtvResponse - path: ${path} """
            )
            Log.v(
                G_P + "StopsSearcherRepositoryReal",
                """sendRequestAndProcessPtvResponse - useHardCodedPvtResponse: $useHardCodedPvtResponse """
            )
            var stopsSearcherObjectsFromJson: StopsSearcherObjectsFromJson? = null
            try {
                if (SharedPreferencesUtility.useHardCodedPvtResponse(context)) {
                    stopsSearcherObjectsFromJson =
                        DebugUtilities(context).getStopsSearcherResponse(path)
                } else {
                    Log.v(
                        G_P + "StopsSearcherRepositoryReal",
                        """sendRequestAndProcessPtvResponse - sending request to the real PTV"""
                    )
                    val getDeparturesDeferred: Deferred<StopsSearcherObjectsFromJson> =
                        PtvApi.retrofitService.getPtvStopsSearcherResponse(path)
                    stopsSearcherObjectsFromJson = getDeparturesDeferred.await()
                }

                // fixLater: Jan 06, 2020 - the 'stopId' is an Int - why do we pass set of strings?
//            val favoriteStopIdsSetTemp = mutableSetOf<Int>()
                val linesAndStopsForSearchResult: LinesAndStopsForSearchResult =
                    StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
                        stopsSearcherObjectsFromJson,
                        favoriteStopIdsSet
                    )

                if (!linesAndStopsForSearchResult.health) {
                    throw java.lang.RuntimeException(context.getString(R.string.ptv_is_not_available))
                }
                val linesStopsDetails =
                    linesAndStopsForSearchResult.lineStopDetailsList
                loadErrMsg.postValue(null)
                lineStopDetailsDao.clearTableAndInsertNewRows(linesStopsDetails)

                /*linesStopsDetails.forEachIndexed { index, lineStopDetails ->
                if (index < 10) {
                    Log.v(G_P + "StopsSearcherRepositoryFake", """sendRequestAndProcessPtvResponse - lineStopDetails: ${lineStopDetails} """)
//                    println(G_P + """StopsSearcherRepositoryFake - sendRequestAndProcessPtvResponse - lineStopDetails: ${lineStopDetails} """)
                }
            }*/
            } catch (e: Exception) {
                Log.v(
                    G_P + "StopsSearcherRepositoryReal",
                    """sendRequestAndProcessPtvResponse - e: ${e} """
                )
                loadErrMsg.postValue("${e.message}")
            } finally {
                isLoading.postValue(false)
            }
        }
    }

    /*
        Toggle view layout - normal/magnified.
    */
    override suspend fun toggleMagnifiedView(id: Int) {
        withContext(Dispatchers.IO) {
            lineStopDetailsDao.toggleMagnifiedNormalView(id)
        }
    }

    override suspend fun getLineStopDetailsCount(): Int {
        var cnt = 0
        withContext(Dispatchers.IO) {
            cnt = lineStopDetailsDao.getLineStopDetailsCount()
        }
        return cnt
    }
}