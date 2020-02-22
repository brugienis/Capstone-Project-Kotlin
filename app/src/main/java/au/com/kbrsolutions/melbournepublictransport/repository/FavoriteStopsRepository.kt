package au.com.kbrsolutions.melbournepublictransport.repository

import androidx.lifecycle.LiveData
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseFavoriteStop
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop

interface FavoriteStopsRepository : Repository {

    fun getFavoriteStops(): LiveData<List<FavoriteStop>>

    suspend fun getFavoriteStop(id: Int): FavoriteStop?

    suspend fun insert(favoriteStop: DatabaseFavoriteStop)

    suspend fun getFavoriteStopsCount(): Int

    suspend fun toggleMagnifiedView(id: Int)

    suspend fun deleteFavoriteStop(stopId: Int)

    suspend fun deleteAllFavoriteStops()

    suspend fun printStopsIds(source: String)
}