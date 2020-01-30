package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseLineStopDetails
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDao
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class StopsSearcherRepositoryReal(private val favoriteStopDao: FavoriteStopDao)
    : StopsSearcherRepository {

    /* map 'id' to LineStopDetails */
    var stopsSearcherServiceData: LinkedHashMap<Int, DatabaseLineStopDetails> = LinkedHashMap()

    private var _stopsSearcherResults = MutableLiveData<List<LineStopDetails>>()

    override fun getStopsSearcherResults(): LiveData<List<LineStopDetails>> = _stopsSearcherResults

    override val loadErrMsg = MutableLiveData<String>()

    /*override val loadErrMsg: MutableLiveData<String>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
*/

    override suspend fun clearTable() {
        stopsSearcherServiceData.clear()
    }

    override suspend fun getLineStopDetails(id: Int): LineStopDetails {
        return stopsSearcherServiceData[id]!!.asDomainModel()
    }

    override suspend fun sendRequestAndProcessPtvResponse(
        path: String,
        favoriteStopIdsSet: Set<String>,
        context: Context) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /*override suspend fun insert(favoriteStop: DatabaseFavoriteStop) {
        withContext(Dispatchers.IO) {
            favoriteStopDao.insert(favoriteStop)
        }
    }*/

    /*override suspend fun getFavoriteStopsCount(): Int {
        var cnt = 0
        withContext(Dispatchers.IO) {
            cnt = favoriteStopDao.getFavoriteStopsCount()
        }
        return cnt
    }*/

    /*
        Toggle view layout - normal/magnified.
    */
    override suspend fun toggleMagnifiedView(id: Int) {
        withContext(Dispatchers.IO) {
            favoriteStopDao.toggleMagnifiedNormalView(id)
        }
    }

    /**
     * Print 'stopIds' of the FavoriteStops in the database.
     */
    /*override suspend fun printStopsIds(source: String) {
        println("FavoriteStopsViewModel - printStopsIds start")
        withContext(Dispatchers.IO) {
            val rows = favoriteStopDao.getDebugFavoriteStops()
//            rows.forEach { println("FavoriteStopsViewModel - printStopsIds - stopId: ${it.stopId}") }
            rows.forEach<DatabaseFavoriteStop> {
                printStopDetails(it.asDomainModel(), "printStopsIds")
            }
        }
    }*/

    private fun printStopDetails(favoriteStop: FavoriteStop, source: String, msg: String = "") {
        println("FavoriteStopsRepositoryReal - printStopDetails - source - $msg stopId: ${favoriteStop.id} stopName: ${favoriteStop.locationName} magnified: ${favoriteStop.showInMagnifiedView}")
    }
}