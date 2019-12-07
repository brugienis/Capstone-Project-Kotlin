package au.com.kbrsolutions.melbournepublictransport.departures

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepository
import au.com.kbrsolutions.melbournepublictransport.utilities.GLOBAL_PREFIX
import au.com.kbrsolutions.melbournepublictransport.utilities.Misc
import kotlinx.coroutines.*

class DeparturesViewModel(
    val departuresFragmentArgs: DeparturesFragmentArgs,
    val context: Context,
    private val departuresRepository: DeparturesRepository) : ViewModel() {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = SupervisorJob()

    // the Coroutine runs using the Main (UI) dispatcher
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)

//    private val ptvDataRepository = PtvDataRepository(dataSource)

    private var _departuresLoadingInProgress = MutableLiveData<Boolean>()

    val departuresLoadingInProgress: LiveData<Boolean>
        get() = departuresRepository.departuresLoadingInProgress

    /*
        The internal MutableLiveData that stores the status of the most recent request
        private val _status = MutableLiveData<DeparturesApiStatus>()

         The external immutable LiveData for the request status
        val status: LiveData<DeparturesApiStatus>
        get() = _status
    */

    private val _loadErrMsg = MutableLiveData<String>()

    val loadErrMsg: LiveData<String>
        get() = departuresRepository.loadErrMsg

    val departureInPtvSortOrder = departuresRepository.getDepartures(
        departuresFragmentArgs.favoriteStopsRequestedTimMillis)

    /**
     * Initialize some 'flags'
     */
    init {
        _departuresLoadingInProgress.value = true
        _loadErrMsg.value = null
    }

    fun loadDepartures() {
        // fixLater: Sep 20, 2019 - get hardcoded values from the SharedPreferences
        val path = DynamicUrlBuilder.buildURI(0, departuresFragmentArgs.stopId, 5,
            setOf("all", "stop", "route", "run", "disruption"))
        uiScope.launch {
            departuresRepository.refreshPtvData(path, context)
        }
    }

    fun sortDepartureDetailsByTime(sortDeparturesByTime: Boolean = true): List<Departure> {
        val departures = departureInPtvSortOrder.value
        return sortDepartures(departures!!, sortDeparturesByTime)
    }

    fun sortDepartures(databaseDepartures: List<Departure>, sortDeparturesByTime: Boolean):
            List<Departure> {
        return  Misc.sortDeparturesData(databaseDepartures, sortDeparturesByTime)
    }

    /**
     * Executes when the 'List View' row is clicked.
     */
    fun onListViewClick(id: Int) {
        Log.v("DeparturesViewModel", """onListViewClick - $id """)
        uiScope.launch {
            toggleMagnifiedView(id)
        }
    }

    /**
     * Executes when the 'databaseDeparture layout id' is clicked - the layout contains specific
     * databaseDeparture and time.
     */
    fun onDepartureLayoutIdClicked(databaseDeparture: Departure) {
        println(GLOBAL_PREFIX + "DeparturesViewModel" + """onDepartureLayoutIdClicked - databaseDeparture: ${databaseDeparture} """)
        uiScope.launch {
            // TODO - missing logic
        }
    }

    /**
     * Executes when the 'stop facility' icon is clicked.
     */
    fun onShowStopFacilityClicked(id: Int) {
        Log.v("DeparturesViewModel", """onShowStopFacilityClicked - $id """)
        uiScope.launch {
            // TODO - missing logic
        }
    }

    /**
     * Executes when the 'disruptions' icon is clicked.
     */
    fun onDisruptionImageIdClicked(databaseDeparture: Departure) {
        Log.v("DeparturesViewModel", """onDisruptionImageIdClicked - $databaseDeparture.id """)
        uiScope.launch {
            // TODO - missing logic
        }
    }

//    var clickedRowViewId: Int? = null

    /*
        Toggle view layout - normal/magnified.
    */
    private suspend fun toggleMagnifiedView(id: Int) {
        withContext(Dispatchers.IO) {
            //            dataSource.flipShowMagnifyView(id)
            departuresRepository.toggleMagnifiedNormalView(id)
//            clickedRowViewId = id

        }
    }

    /**
     * Cancels all coroutines when the ViewModel is cleared, to cleanup any pending work.
     *
     * onCleared() gets called when the ViewModel is destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // fixLater: Aug 23, 2019 - remove below after testing
    /*private fun showRequestUrl() {
        val callBack = PtvApi.retrofitService.getPtvResponse(testBuildUrl())
        val requestUrl = callBack.request().url().toString()
        Log.v("showRequestUrl", """requestUrl: ${requestUrl} """)
    }*/

    // fixLater: Aug 23, 2019 - remove below after testing
    /*private fun testBuildUrl(): String {
        val result = DynamicUrlBuilder.buildURI(0,"1035", 10, emptySet())
        Log.v("DeparturesViewModel", """testBuildUrl - result: ${result} """)
        return result
    }*/

}
