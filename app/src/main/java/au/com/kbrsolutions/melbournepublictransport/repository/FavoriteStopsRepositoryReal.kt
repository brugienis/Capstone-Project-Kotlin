package au.com.kbrsolutions.melbournepublictransport.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDao
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This class is used only in the 'prod' variant. It is returned by the ServiceLocator that is in
 * the 'prod' path.
 */
class FavoriteStopsRepositoryReal(private val favoriteStopDao: FavoriteStopDao)
    : FavoriteStopsRepository {

    override fun getFavoriteStops(): LiveData<List<FavoriteStop>> =
        Transformations.map(favoriteStopDao.getFavoriteStops()) {
            it.asDomainModel()
        }

    override suspend fun insert(favoriteStop: DatabaseFavoriteStop) {
        withContext(Dispatchers.IO) {
            favoriteStopDao.insert(favoriteStop)
        }
    }

    override suspend fun getFavoriteStopsCount(): Int {
        var cnt = 0
        withContext(Dispatchers.IO) {
            cnt = favoriteStopDao.getFavoriteStopsCount()
        }
        return cnt
    }

    /*
        Toggle view layout - normal/magnified.
    */
    override suspend fun toggleMagnifiedView(id: Int) {
        withContext(Dispatchers.IO) {
            favoriteStopDao.toggleMagnifiedNormalView(id)
        }
    }

    /*
        Delete all rows in the 'favorite_stop' table.
    */
    override suspend fun deleteAllFavoriteStops() {
        withContext(Dispatchers.IO) {
            favoriteStopDao.clear()
        }
    }

    /*
        Delete one row in the 'favorite_stop' table.
    */
    override suspend fun deleteFavoriteStop(stopId: Int) {
        withContext(Dispatchers.IO) {
            favoriteStopDao.deleteFavoriteStop(stopId)
        }
    }

    @VisibleForTesting
    override suspend fun getFavoriteStop(id: Int): FavoriteStop? {
        return favoriteStopDao.getFavoriteStop(id)?.asDomainModel()
    }

    /**
     * Print 'stopIds' of the FavoriteStops in the database.
     */
    override suspend fun printStopsIds(source: String) {
        println("FavoriteStopsViewModel - printStopsIds start")
        withContext(Dispatchers.IO) {
            val rows = favoriteStopDao.getDebugFavoriteStops()
//            rows.forEach { println("FavoriteStopsViewModel - printStopsIds - stopId: ${it.stopId}") }
            rows.forEach<DatabaseFavoriteStop> {
                printStopDetails(it.asDomainModel(), "printStopsIds")
            }
        }
    }

    private fun printStopDetails(favoriteStop: FavoriteStop, source: String, msg: String = "") {
        println("FavoriteStopsRepositoryReal - printStopDetails - source - $msg stopId: ${favoriteStop.id} stopName: ${favoriteStop.locationName} magnified: ${favoriteStop.showInMagnifiedView}")
    }

}