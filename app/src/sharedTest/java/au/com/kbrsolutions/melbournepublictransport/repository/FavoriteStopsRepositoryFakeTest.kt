package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.ServiceLocator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteStopsRepositoryFakeTest {
    private lateinit var repository: FavoriteStopsRepositoryFake
    private lateinit var context: Context
    private lateinit var favoriteStopsRepositoryTests: FavoriteStopsRepositoryTests

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRepository() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = FavoriteStopsRepositoryFake().apply { setSimulatedDelayMillis(3_000) }
        ServiceLocator.favoriteStopsRepository = repository
        favoriteStopsRepositoryTests =
            FavoriteStopsRepositoryTests(repository, context, false)
    }

    @After
    @ExperimentalCoroutinesApi
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetFavoriteStopsRepository()
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