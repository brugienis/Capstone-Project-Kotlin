package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking

class StopsSarcherRepositoryTests(
    val repository: StopsSearcherRepository,
    val context: Context,
    val isRealRepo: Boolean) {

    fun getStopsSearcherResults_responseHealthFalse() {
        loadRepository("stops_searcher_2_stops_health_false.json")
        repository.getStopsSearcherResults()
        val loadErrMsg = repository.loadErrMsg.getOrAwaitValue()
        Truth.assertThat(loadErrMsg).isEqualTo(context.getString(R.string.ptv_is_not_available))
    }

    fun getStopsSearcherResults_find2StopsOnly() {
        loadRepository("stops_searcher_2_stops_only.json")
        val liveData = repository.getStopsSearcherResults()
        val loadErrMsg = repository.loadErrMsg.getOrAwaitValue()
        Truth.assertThat(loadErrMsg).isEqualTo(null)
        val lineStopDetailsList = liveData.getOrAwaitValue()
        Truth.assertThat(lineStopDetailsList.size).isEqualTo(2)
    }

    fun clearTable() {
        loadRepository("stops_searcher_2_stops_only.json")
        val searchResultsLiveData= repository.getStopsSearcherResults()
        val loadErrMsg = repository.loadErrMsg.getOrAwaitValue()
        Truth.assertThat(loadErrMsg).isEqualTo(null)
        var searchResults = searchResultsLiveData.getOrAwaitValue()
        Truth.assertThat(searchResults.size).isEqualTo(2)
        var rowsCnt = 0
        runBlocking {
            repository.clearTable()
            rowsCnt = repository.getLineStopDetailsCount()
        }
        Truth.assertThat(rowsCnt).isEqualTo(0)
    }

    fun getLineStopDetails() {
        loadRepository("stops_searcher_2_stops_only.json")
        var lineStopDetails: LineStopDetails? = null
        runBlocking {
            lineStopDetails = repository.getLineStopDetails(1)
        }
        Truth.assertThat(lineStopDetails!!.stopLocationOrLineName).isEqualTo("Frankston Station")
    }

    fun toggleMagnifiedView() {
        loadRepository("stops_searcher_2_stops_only.json")
        var lineStopDetails: LineStopDetails? = null
        runBlocking {
            lineStopDetails = repository.getLineStopDetails(1)
            Truth.assertThat(lineStopDetails!!.showInMagnifiedView).isEqualTo(false)
            repository.toggleMagnifiedView(1)
            lineStopDetails = repository.getLineStopDetails(1)
            Truth.assertThat(lineStopDetails!!.showInMagnifiedView).isEqualTo(true)
        }
    }

    private fun loadRepository(jsonFileName: String) {
        if (!isRealRepo) {
            (repository as StopsSearcherRepositoryFake).setDebuggingJsonStringFile(jsonFileName)
        }
        runBlocking {
            repository.sendRequestAndProcessPtvResponse(
                jsonFileName,
                setOf(),
                context
            )
        }
    }

}