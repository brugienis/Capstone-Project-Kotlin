package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository
import au.com.kbrsolutions.melbournepublictransport.utilities.GLOBAL_PREFIX
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility.setShowStopSearcherInstructions
import kotlinx.coroutines.*


class StopsSearcherViewModel(
    private val favoriteStopsRepository: FavoriteStopsRepository,
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

    private var favoriteStops = MutableLiveData<List<FavoriteStop>>()   // = database.getDepartures()

    private var _favoriteStopsMsg = MutableLiveData<String>()

    private val _showInstructions = MutableLiveData<Boolean>()

    val showInstructions: LiveData<Boolean>
        get() = _showInstructions

    fun insertFavoriteStop() {
        uiScope.launch {
            insertStopDetails()
        }
    }

    // fixLater: Dec 01, 2019 - check out https://medium.com/@dimabatyuk/adding-clear-button-to-edittext-9655e9dbb721
    // fixLater: Dec 01, 2019 - check out https://github.com/DmytroBatyuk/Clearable-EditText-Implementation/blob/master/app/src/main/java/ua/batyuk/dmytro/clearableedittextimplementation/EditText.kt#L70


    private fun getRoute(): Int {
        return rowCnt.rem(3)
    }

    @SuppressLint("LongLogTag")
    fun startLineAndStopsSearch(searchText: String) {
        Log.v(GLOBAL_PREFIX + "StopsSearcherViewModel", """startLineAndStopsSearch - searchText: ${searchText} """)
    }

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
    private var rowCnt = -1
    private suspend fun insertStopDetails() {
        rowCnt = favoriteStopsRepository.getFavoriteStopsCount()
        Log.v("StopsSearcherViewModel", """insertStopDetails - before rowCnt: ${rowCnt} """)
        var routeType = getRoute()
        val favoriteStop = if (rowCnt == 0) {
            DatabaseFavoriteStop(
                rowCnt++,
                0,
                "1035",
                """Carrum""",
                "Carrum",
                1.0,
                10.0
            )
        } else {
            DatabaseFavoriteStop(
                rowCnt++,
                routeType,
                "stopId$routeType",
                """loc A: $routeType""",
                "sub A$routeType",
                1.0,
                10.0
            )
        }
        withContext(Dispatchers.IO) {
            favoriteStopsRepository.insert(favoriteStop)
        }
        Log.v("StopsSearcherViewModel", """insertStopDetails - after  rowCnt: ${rowCnt} """)
        favoriteStopsRepository.printStopsIds("StopsSearcherViewModel - insertStopDetails")
        return
    }

}