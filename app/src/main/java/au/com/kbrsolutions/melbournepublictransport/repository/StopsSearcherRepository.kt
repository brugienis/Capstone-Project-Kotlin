package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails

interface StopsSearcherRepository : Repository {

    suspend fun clearTable()

    fun getStopsSearcherResults(): LiveData<List<LineStopDetails>>

    suspend fun getLineStopDetails(id: Int): LineStopDetails

    val loadErrMsg: MutableLiveData<String>

    suspend fun sendRequestAndProcessPtvResponse(
        path: String,
        favoriteStopIdsSet: Set<String>,
        context: Context
    )

//    suspend fun insert(favoriteStop: DatabaseFavoriteStop)

//    suspend fun getFavoriteStopsCount(): Int

    suspend fun toggleMagnifiedView(id: Int)

//    suspend fun printStopsIds(source: String)
}