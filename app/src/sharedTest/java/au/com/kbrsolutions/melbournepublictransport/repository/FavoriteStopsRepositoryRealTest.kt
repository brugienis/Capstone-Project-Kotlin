package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.ServiceLocator
import au.com.kbrsolutions.melbournepublictransport.data.AppDatabase
import au.com.kbrsolutions.melbournepublictransport.data.FavoriteStopDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteStopsRepositoryRealTest {
    private lateinit var database: AppDatabase
    private lateinit var favoriteStopDao: FavoriteStopDao
    private lateinit var repository: FavoriteStopsRepositoryReal
    private lateinit var context: Context
    private lateinit var favoriteStopsRepositoryTests: FavoriteStopsRepositoryTests

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        favoriteStopDao = database.favoriteStopDao()
        repository = FavoriteStopsRepositoryReal(favoriteStopDao)
        ServiceLocator.favoriteStopsRepository = repository
        favoriteStopsRepositoryTests =
            FavoriteStopsRepositoryTests(repository, context, true)
    }

    @After
    @ExperimentalCoroutinesApi
    fun closeDb() {
        fun cleanupDb() = runBlockingTest {
            database.close()
            ServiceLocator.resetFavoriteStopsRepository()
        }
    }

    @Test
    fun getFavoriteStops() {
        favoriteStopsRepositoryTests.getFavoriteStops()
    }

    @Test
    fun insert() {
        favoriteStopsRepositoryTests.insert()
    }

    @Test
    fun toggleMagnifiedView_insertRowAndToggle() {
        favoriteStopsRepositoryTests.toggleMagnifiedView_insertRowAndToggle()
    }

    @Test
    fun toggleMagnifiedView_insert3RowsToggle2ndDeleteToggleLast() {
        favoriteStopsRepositoryTests.toggleMagnifiedView_insert3RowsToggle2ndDeleteToggleLast()
    }

    @Test
    fun toggleMagnifiedView_insert3RowsToggle2ndToggleLast() {
        favoriteStopsRepositoryTests.toggleMagnifiedView_insert3RowsToggle2ndToggleLast()
    }

    @Test
    fun deleteFavoriteStop() {
        favoriteStopsRepositoryTests.deleteFavoriteStop()
    }

    @Test
    fun deleteAllFavoriteStops() {
        favoriteStopsRepositoryTests.deleteAllFavoriteStops()
    }

}