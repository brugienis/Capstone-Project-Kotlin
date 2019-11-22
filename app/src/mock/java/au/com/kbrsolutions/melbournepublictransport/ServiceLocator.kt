package au.com.kbrsolutions.melbournepublictransport

import android.content.Context
import androidx.annotation.VisibleForTesting
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepository
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepository
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import kotlinx.coroutines.runBlocking

object ServiceLocator {
    private val lock = Any()
    @Volatile var favoriteStopsRepository: FavoriteStopsRepository? = null
        @VisibleForTesting set

    fun provideFavoriteStopsRepository(context: Context): FavoriteStopsRepository {
        return favoriteStopsRepository ?: synchronized(this) {
            favoriteStopsRepository ?: createTasksRepository(context).also {
                favoriteStopsRepository = it
            }
        }
    }

    private fun createTasksRepository(context: Context): FavoriteStopsRepository {
        return FavoriteStopsRepositoryFake()
    }

    @VisibleForTesting
    fun resetFavoriteStopsRepository() {
        synchronized(lock) {
            runBlocking {
                favoriteStopsRepository?.deleteAllFavoriteStops()
            }
            favoriteStopsRepository = null
        }
    }

    @Volatile var departuresRepository: DeparturesRepository? = null
        @VisibleForTesting set

    fun provideDeparturersRepository(context: Context): DeparturesRepository {
        return departuresRepository ?: synchronized(this) {
            departuresRepository ?: createDeparturesRepository(context).also {
                departuresRepository = it
            }
        }
    }

    private fun createDeparturesRepository(context: Context): DeparturesRepository {
        return DeparturesRepositoryFake()
    }

    @VisibleForTesting
    fun resetDeparturesRepository() {
        synchronized(lock) {
            runBlocking {
                departuresRepository?.deleteAllDepartures()
            }
            departuresRepository = null
        }
    }
}