package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository
import au.com.kbrsolutions.melbournepublictransport.repository.StopsSearcherRepository
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.Miscellaneous
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility.getRouteTypes
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility.setShowStopSearcherInstructions
import au.com.kbrsolutions.melbournepublictransport.utilities.hideSoftKeyboardForView
import kotlinx.coroutines.*

enum class ShowView {
    SearchResults,
    InfoText,
    Instructions
}

class StopsSearcherViewModel(
    private val stopsSearcherFragmentArgs: StopsSearcherFragmentArgs,
    private val favoriteStopsRepository: FavoriteStopsRepository,
    private val stopsSearcherRepository: StopsSearcherRepository,
    private val context: Context) : ViewModel() {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _showView = MutableLiveData<ShowView>(enumValueOf(ShowView.Instructions.toString()))
    val showView: LiveData<ShowView>
        get() = _showView

    private val _showInstructions = MutableLiveData<Boolean>()

    val showInstructions: LiveData<Boolean>
        get() = _showInstructions

    private val _navigateUp = MutableLiveData<Boolean>(false)

    val navigateUp: LiveData<Boolean>
        get() = _navigateUp

    private val _searchTextHint = MutableLiveData<String>()

    val searchTextHint: LiveData<String>
        get() = _searchTextHint

    private val _stopSearcherTextValidationMsg = MutableLiveData<String>()

    val stopSearcherTextValidationMsg: LiveData<String>
        get() = _stopSearcherTextValidationMsg

    val loadErrMsg: LiveData<String>
        get() = stopsSearcherRepository.loadErrMsg

    private val _clearMsgClicked = MutableLiveData<Boolean>()

    val clearMsgClicked: LiveData<Boolean>
        get() = _clearMsgClicked

    val stopsSearchResults = stopsSearcherRepository.getStopsSearcherResults()
    val favoriteStopsArray = stopsSearcherFragmentArgs.favoriteStopsArray

    init {
        Log.v(G_P + "StopsSearcherViewModel", """ - favoriteStopsArray: ${favoriteStopsArray.size} """)
        favoriteStopsArray.forEach {
            Log.v(G_P + "StopsSearcherViewModel", """ - favStopId: ${it} """)
        }
        _clearMsgClicked.value = false
        _searchTextHint.value = context.resources.getString(R.string.enter_search_text)
//        _showView.value = ShowView.Instructions
        clearValidationMsg()
    }

    fun setShownView(viewEnum: ShowView) {
        _showView.value = viewEnum
        Log.v(G_P + "StopsSearcherViewModel", """setShownView - viewEnum: ${viewEnum} """)
    }

    fun navigateUpDone() {
        _navigateUp.value = false
//        clearRepositoryTables()
    }

    // fixLater: Dec 01, 2019 - check out https://medium.com/@dimabatyuk/adding-clear-button-to-edittext-9655e9dbb721
    // fixLater: Dec 01, 2019 - check out https://github.com/DmytroBatyuk/Clearable-EditText-Implementation/blob/master/app/src/main/java/ua/batyuk/dmytro/clearableedittextimplementation/EditText.kt#L70

    /*private fun getRoute(): Int {
        return rowCnt.rem(3)
    }*/

    fun cancelStopsSearch() {
//        Log.v(G_P + "StopsSearcherViewModel", """cancelStopsSearch - start""")
        _searchTextHint.value = context.resources.getString(R.string.search_cancelled_enter_search_text)
    }

    fun clearSearchTextClicked() {
        Log.v(G_P + "StopsSearcherViewModel", """clearSearchTextClicked - clearSearchTextClicked""")
        _clearMsgClicked.value = true
        clearValidationMsg()
        _searchTextHint.value = context.resources.getString(R.string.enter_search_text)
    }

    private fun clearValidationMsg() {
        if (_stopSearcherTextValidationMsg.value == null ||
            _stopSearcherTextValidationMsg.value!!.isNotEmpty()) {
            _stopSearcherTextValidationMsg.value = ""
        }
    }

    /**
     * Called when user pressed 'search' button. Starts search if search text is not empty.
     */
    fun handleSearchButtonClicked(view: View, actionId: Int, searchText: String?): Boolean {
//        Log.v(G_P + "StopsSearcherViewModel", """handleSearchButtonClicked - actionId: ${actionId} """)
        var handled = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (searchText != null) {
                validateSearchTextAndStartSearch(searchText)
                handled = true
                hideSoftKeyboardForView(view)
            } else {
//                setStopsSearchText("")
            }
        }
        return handled
    }

    /**
     * Start stops search if searchText is valid, otherwise show validation message.
     *
     * @param searchText    search text
     */
    fun validateSearchTextAndStartSearch(searchText: String) {
        var locSearchText: String = searchText
        if (Miscellaneous.validateSearchText(locSearchText)) {
            locSearchText = Miscellaneous.capitalizeWords(locSearchText)
            startLineAndStopsSearch(locSearchText)
//            showInfoText(getString(R.string.searching_in_progress))
            _searchTextHint.value = context.resources.getString(R.string.searching_in_progress)
            clearValidationMsg()
        } else {
//            showInfoText(getString(R.string.invalid_search_text))
            _stopSearcherTextValidationMsg.value = context.resources.getString(R.string.invalid_search_text)
        }
    }

    @SuppressLint("LongLogTag")
    fun startLineAndStopsSearch(searchText: String) {
        val delayMillis = 3_000L
        val routTypes = getRouteTypes(context)
        val path = StopsSearcherUrlBuilder.buildURI(
            searchText,
            null,
            40000f,
            routTypes
        )
//        EspressoIdlingResource.increment("StopsSearcherViewModel.startLineAndStopsSearch")
        val favoriteStopIdsSet = mutableSetOf<Int>()
        Log.v(G_P + "StopsSearcherViewModel", """startLineAndStopsSearch - path: ${path} """)
        uiScope.launch {
            /*runBlocking {
                delay(delayMillis)
            }*/

            /*add code to build favoriteStopIdsSet - existing Favorite Stops in the database
                    Better - pass the set of stop ids from FavoriteStops in arguments*/

//            favoriteStopsArray.forEach {
                favoriteStopIdsSet.addAll(favoriteStopsArray.asList())
//            }
            stopsSearcherRepository.sendRequestAndProcessPtvResponse(
                path,
                favoriteStopIdsSet,
                context
            )
        }
    }

    /**
     * Executes when the 'List View' row is clicked.
     */
    fun onListItemViewClick(id: Int) {
        Log.v("StopsSearcherViewModel", """onListViewClick - $id """)
        uiScope.launch {
            toggleMagnifiedView(id)
        }
    }

    /*
        Toggle view layout - normal/magnified.
    */
    private suspend fun toggleMagnifiedView(id: Int) {
        withContext(Dispatchers.IO) {
            stopsSearcherRepository.toggleMagnifiedView(id)
        }
    }

    /**
     * Executes when the 'stop facility' icon is clicked.
     */
    fun onAddStopOrGetStopsOnlineClicked(id: Int) {
        Log.v("StopsSearcherViewModel", """onAddStopOrGetStopsOnlineClicked - $id """)
        uiScope.launch {
            insertStopDetails(stopsSearcherRepository.getLineStopDetails(id))
        }
    }

    /*fun insertFavoriteStop() {
        uiScope.launch {
            insertStopDetails(stopsSearcherRepository.getLineStopdetails(id))
        }
    }*/

    fun toggleShowHideNotes() {
        setShowStopSearcherInstructions(
            context,
            !SharedPreferencesUtility.isShowStopSearcherInstructions(context)
        )
        _showInstructions.value = SharedPreferencesUtility.isShowStopSearcherInstructions(context)
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private suspend fun insertStopDetails(lineStopDetails: LineStopDetails) {
//        var routeType = getRoute()
        val favoriteStop =
            DatabaseFavoriteStop(
                lineStopDetails.id,
                lineStopDetails.routeType,
                lineStopDetails.stopId,
                lineStopDetails.stopLocationOrLineName,
                lineStopDetails.suburb ?: "",
                lineStopDetails.latitude,
                lineStopDetails.longitude
            )
        withContext(Dispatchers.IO) {
            favoriteStopsRepository.insert(favoriteStop)
        }
        context.applicationContext
        Log.v("StopsSearcherViewModel", """insertStopDetails - favoriteStop: $favoriteStop """)
        _navigateUp.value = true
        return
    }

    fun clearRepositoryTables() {
        uiScope.launch {
            stopsSearcherRepository.clearTable()
        }
    }

}