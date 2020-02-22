package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import android.util.Log
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking

class DeparturesRepositoryTests(
    val repository: DeparturesRepository,
    val context: Context,
    val isRealRepo: Boolean) {

    fun testLoadErrMsg() = runBlocking {
        loadRepository("departures_results_sample.json")
//        repository.refreshPtvData("departures_results_sample.json", context)
        var loadErrMsgLD = repository.loadErrMsg
        var loadErrMsg= loadErrMsgLD.getOrAwaitValue()
        Truth.assertThat(loadErrMsg).isNull()

        loadRepository("departures_results_sample_health_false.json")
//        repository.refreshPtvData("departures_results_sample_health_false.json", context)
        loadErrMsgLD = repository.loadErrMsg
        loadErrMsg= loadErrMsgLD.getOrAwaitValue()
        Truth.assertThat(loadErrMsg).isNotNull()
        Truth.assertThat(loadErrMsg).isEqualTo(context.getString(R.string.ptv_is_not_available))
    }

    fun getDeparturesLoadingInProgress() {
    }

    // fixLater: Feb 11, 2020 - remove commented lines
    fun getDepartures() = runBlocking {
        loadRepository("departures_results_sample.json")
//        repository.refreshPtvData("departures_results_sample.json", context)
        val loadErrMsgLD = repository.loadErrMsg
        val loadErrMsg= loadErrMsgLD.getOrAwaitValue()
        Log.v(G_P + "DeparturesRepositoryTests", """getDepartures - loadErrMsg: ${loadErrMsg} """)
        Truth.assertThat(loadErrMsg).isNull()
        val departures = repository.getDepartures(0L)
        Truth.assertThat(departures.getOrAwaitValue().size).isEqualTo(5)
    }

    fun toggleMagnifiedNormalView() = runBlocking {
        loadRepository("departures_results_sample.json")
//        repository.refreshPtvData("departures_results_sample.json", context)
        var databaseDeparture = repository.getDeparture(1)
        Truth.assertThat(databaseDeparture!!.showInMagnifiedView).isEqualTo(false)
        repository.toggleMagnifiedNormalView(1)
        databaseDeparture = repository.getDeparture(1)
        Truth.assertThat(databaseDeparture!!.showInMagnifiedView).isEqualTo(true)
    }

    fun deleteAllDepartures() = runBlocking {
        loadRepository("departures_results_sample.json")
//        repository.refreshPtvData("departures_results_sample.json", context)
        val loadErrMsgLD = repository.loadErrMsg
        val loadErrMsg= loadErrMsgLD.getOrAwaitValue()
        Log.v(G_P + "DeparturesRepositoryTests", """getDepartures - loadErrMsg: ${loadErrMsg} """)
        Truth.assertThat(loadErrMsg).isNull()
        var departures = repository.getDepartures(0L)
        Truth.assertThat(departures.getOrAwaitValue().size).isEqualTo(5)
        repository.deleteAllDepartures()
        departures = repository.getDepartures(0L)
        Truth.assertThat(departures.getOrAwaitValue().size).isEqualTo(0)
    }

    private fun loadRepository(jsonFileName: String) {
        if (!isRealRepo) {
            (repository as DeparturesRepositoryFake).setDebuggingJsonStringFile(jsonFileName)
        }
        runBlocking {
            repository.refreshPtvData(jsonFileName, context)
        }
    }
}