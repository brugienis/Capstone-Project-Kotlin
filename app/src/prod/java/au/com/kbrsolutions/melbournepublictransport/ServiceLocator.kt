package au.com.kbrsolutions.melbournepublictransport

import android.content.Context
import androidx.annotation.VisibleForTesting
import au.com.kbrsolutions.melbournepublictransport.data.AppDatabase
import au.com.kbrsolutions.melbournepublictransport.repository.*
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    private val lock = Any()
    private var database: AppDatabase? = null
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
        database = AppDatabase.getInstance(context)

        return FavoriteStopsRepositoryReal(database!!.favoriteStopDao())
    }

    @VisibleForTesting
    fun resetFavoriteStopsRepository() {
        synchronized(lock) {
            runBlocking {
                favoriteStopsRepository?.deleteAllFavoriteStops()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
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
        database = AppDatabase.getInstance(context)

        return DeparturesRepositoryReal(database!!.departureDao())
    }

    @VisibleForTesting
    fun resetDeparturesRepository() {
        synchronized(lock) {
            runBlocking {
                departuresRepository?.deleteAllDepartures()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            departuresRepository = null
        }
    }

    @Volatile var stopsSearcherRepository: StopsSearcherRepository? = null
        @VisibleForTesting set

    fun provideStopsSearcherRepository(context: Context): StopsSearcherRepository {
        return stopsSearcherRepository ?: synchronized(this) {
            stopsSearcherRepository ?: createStopsSearcherRepository(context).also {
                stopsSearcherRepository = it
            }
        }
    }

    private fun createStopsSearcherRepository(context: Context): StopsSearcherRepository {
        return StopsSearcherRepositoryReal(database!!.lineStopDetailsDao())
    }

    @VisibleForTesting
    fun resetStopsSearcherRepository() {
        synchronized(lock) {
            runBlocking {
                favoriteStopsRepository?.deleteAllFavoriteStops()
            }
            favoriteStopsRepository = null
        }
    }

}