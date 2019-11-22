package au.com.kbrsolutions.melbournepublictransport.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * This class is used only in the 'mock' variant. It is returned by the ServiceLocator that is in
 * the 'mock' path.
 */
class FavoriteStopsRepositoryFake() : FavoriteStopsRepository {

    /* map 'id' to FavoriteStop */
    var favoriteStopsServiceData: LinkedHashMap<Int, FavoriteStop> = LinkedHashMap()

    private var _favoriteStops = MutableLiveData<List<FavoriteStop>>()

    private var delayMillis = 0L

    fun setSimulatedDelayMillis(delayMillis: Long) {
        this.delayMillis = delayMillis
    }

    override fun getFavoriteStops(): LiveData<List<FavoriteStop>> = _favoriteStops

    override suspend fun insert(favoriteStop: DatabaseFavoriteStop) {
        favoriteStopsServiceData[favoriteStop.id] = favoriteStop.asDomainModel()
        updateLiveData()
    }

    override suspend fun getFavoriteStopsCount(): Int {
        return favoriteStopsServiceData.size
    }

    /*
        Toggle view layout - normal/magnified.
    */
    override suspend fun toggleMagnifiedView(id: Int) {
        runBlocking {
            val magnifiedFavoriteStopList = favoriteStopsServiceData.values.filter {
                it.showInMagnifiedView == true
            }
            val listSize = magnifiedFavoriteStopList.size
            if (listSize > 1) {
                throw RuntimeException("""BR - 
                    |FavoriteStopsRepositoryFake.toggleMagnifiedNormalView(): 
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
            val favoriteStop: FavoriteStop = favoriteStopsServiceData[id]!!
            printStopDetails(favoriteStopsServiceData[id]!!, "before")
            favoriteStopsServiceData[id] = FavoriteStop(
                favoriteStop.id,
                favoriteStop.routeType,
                favoriteStop.stopId,
                favoriteStop.locationName,
                favoriteStop.suburb,
                favoriteStop.latitude,
                favoriteStop.longitude,
                !favoriteStop.showInMagnifiedView
            )
            updateLiveData()
        }
    }

    /**
     *  Delete one row in the 'favorite_stop' table.
     */
    override suspend fun deleteFavoriteStop(stopId: String) {
        runBlocking {
            delay(delayMillis)
            val favoriteStops = favoriteStopsServiceData.keys.filter {
                favoriteStopsServiceData[it]!!.stopId == stopId
            }
            favoriteStopsServiceData.remove(favoriteStops[0])
            updateLiveData()
        }
    }

    /**
     *  Delete all rows in the 'favorite_stop' table.
     */
    override suspend fun deleteAllFavoriteStops() {
        runBlocking {
            delay(delayMillis)
            favoriteStopsServiceData.clear()
            updateLiveData()
        }
    }

    /**
     * Print 'stopIds' of the FavoriteStops in the database.
     */
    override suspend fun printStopsIds(source: String) {
        println("FavoriteStopsRepositoryFake - printStopsIds start")
        favoriteStopsServiceData.keys.forEach {
            printStopDetails(favoriteStopsServiceData[it]!!, source)
        }
        println("FavoriteStopsRepositoryFake - printStopsIds end")
    }

    private fun printStopDetails(favoriteStop: FavoriteStop, source: String, msg: String = "") {
        println("FavoriteStopsRepositoryFake - printStopDetails - source - $msg stopId: ${favoriteStop.id} stopName: ${favoriteStop.locationName} magnified: ${favoriteStop.showInMagnifiedView}")
    }

    /**
     * Print 'stopIds' of the FavoriteStops in the database.
     */
    private fun localPrintStopsIds(source: String) {
        if (favoriteStopsServiceData == null) {
            println("FavoriteStopsRepositoryFake - localPrintStopsIds favoriteStopsServiceData is null")
            return
        }
        println("FavoriteStopsRepositoryFake - localPrintStopsIds start")
        favoriteStopsServiceData.keys.forEach {
            printStopDetails(favoriteStopsServiceData[it]!!, source)
        }
        println("FavoriteStopsRepositoryFake - localPrintStopsIds end")
    }

    @VisibleForTesting
    fun addFavoriteStops(vararg favoriteStops: FavoriteStop) {
        for (favoriteStop in favoriteStops) {
            favoriteStopsServiceData[favoriteStop.id] = favoriteStop
        }
        updateLiveData()
    }

    private fun updateLiveData() {
        val favoriteStopsList = mutableListOf<FavoriteStop>()
        favoriteStopsServiceData.keys.forEach {
            favoriteStopsList.add(favoriteStopsServiceData[it]!!)
        }
        _favoriteStops.postValue(favoriteStopsList)
    }
}

