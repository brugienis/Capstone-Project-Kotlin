package au.com.kbrsolutions.melbournepublictransport.favoritestops

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FavoriteStopsViewModel(
//    val favoriteStopsRepository: FavoriteStopsRepository, application: Application) : AndroidViewModel(application) {
    val favoriteStopsRepository: FavoriteStopsRepository) : ViewModel() {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this viewModelScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in viewModelScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
//    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var favoriteStops: LiveData<List<FavoriteStop>> = favoriteStopsRepository.getFavoriteStops()
//    val favoriteStops: LiveData<List<FavoriteStop>> = viewModelScope.launch { favoriteStopsRepository.getFavoriteStops() }

    /**
     * Variable that tells the Fragment to navigate to a specific [FavoriteStopsFragment]
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */

    private val _navigateToNextDepartures = MutableLiveData<FavoriteStop>()

    /**
     * If this is non-null, immediately navigate to [FavoriteStopsFragment] and call [doneNavigating]
     */
    val navigateToNextDepartures: LiveData<FavoriteStop>
        get() = _navigateToNextDepartures

    /**
     * Call this immediately after navigating to [DeparturesFragment]
     *
     * It will deleteAllFavoriteStops the navigation request, so if the user rotates their phone it won't navigate
     * twice.
     */
    fun doneNavigating() {
        _navigateToNextDepartures.value = null
    }

    /**
     * Build array of Favorite stops ids. It will be used in Stops Searcher to remove those stops
     * from search results.
     */
    fun buildFavoriteStopsArray(): IntArray {
        var favoriteStopsArray = IntArray(0)
        favoriteStops.value?.let {
            favoriteStopsArray = IntArray(it.size) { i:Int -> it[i].stopId}
        }
        return favoriteStopsArray
    }

    /**
     * Executes when the 'List View' row is clicked.
     */
    fun onListViewClick(id: Int) {
//        Log.v("FavoriteStopsViewModel", """onListViewClick - favoriteStop: $id """)
        viewModelScope.launch {
            favoriteStopsRepository.toggleMagnifiedView(id)
        }
    }

    /**
     * Executes when the 'garbage' icon is clicked - delete one FavoriteStop.
     */
    fun onDeleteFavoriteStop(stopId: Int) {
        viewModelScope.launch {
            favoriteStopsRepository.deleteFavoriteStop(stopId)
        }
    }

    /**
     * Executes when the 'map' icon is clicked.
     */
    fun onShowStopOnMapClicked(stopId: Int) {
        Log.v("FavoriteStopsViewModel", """onShowStopOnMapClicked - favoriteStop: $stopId """)
        viewModelScope.launch {
            // TODO - missing logic
        }
    }

    /**
     * Executes when the 'departures' icon is clicked.
     */
    fun onShowDeparturesClicked(favoriteStop: FavoriteStop) {
//        Log.v("FavoriteStopsViewModel", """onShowDeparturesClicked - favoriteStop: $favoriteStop.stopId """)
        viewModelScope.launch {
            EspressoIdlingResource.increment("FavoriteStopsViewModel.onShowDeparturesClicked")
            _navigateToNextDepartures.value = favoriteStop
        }
    }

    /**
     * Executes when the 'stop facility' icon is clicked.
     */
    fun onShowStopFacilityClicked(stopId: Int) {
        Log.v("FavoriteStopsViewModel", """onShowStopFacilityClicked - favoriteStop: $stopId """)
        viewModelScope.launch {
            // TODO - missing logic
        }
    }

    /**
     * Use only for debugging.
     */
    fun onPrintStopsIds() {
        Log.v("FavoriteStopsViewModel", """onPrintStopsIds """)
        viewModelScope.launch {
            favoriteStopsRepository.printStopsIds("FavoriteStopsViewModel - onPrintStopsIds")
        }
    }

    /**
     * Executes when the 'delete all favorite stops' menu item is clicked.
     */
    fun onClear() {
        viewModelScope.launch {
            favoriteStopsRepository.deleteAllFavoriteStops()
        }
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

}