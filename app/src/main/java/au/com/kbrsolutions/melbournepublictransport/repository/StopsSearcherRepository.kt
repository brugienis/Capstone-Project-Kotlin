package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails

interface StopsSearcherRepository : Repository {

    suspend fun clearTable()

    fun getStopsSearcherResults(): LiveData<List<LineStopDetails>>

    suspend fun getLineStopDetailsCount(): Int

    suspend fun getLineStopDetails(id: Int): LineStopDetails

    val loadErrMsg: MutableLiveData<String>

    val isLoading: MutableLiveData<Boolean>

    suspend fun sendRequestAndProcessPtvResponse(
        path: String,
        favoriteStopIdsSet: Set<Int>,
        context: Context)

    suspend fun toggleMagnifiedView(id: Int)

}