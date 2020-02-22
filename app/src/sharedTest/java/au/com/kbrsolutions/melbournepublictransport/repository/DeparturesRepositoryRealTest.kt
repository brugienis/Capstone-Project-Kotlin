package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.ServiceLocator
import au.com.kbrsolutions.melbournepublictransport.data.AppDatabase
import au.com.kbrsolutions.melbournepublictransport.data.DepartureDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DeparturesRepositoryRealTest {
    private lateinit var database: AppDatabase
    private lateinit var departureDao: DepartureDao
    private lateinit var repository: DeparturesRepositoryReal
    private lateinit var context: Context
    private lateinit var departuresRepositoryTests: DeparturesRepositoryTests

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
        departureDao = database.departureDao()
        repository = DeparturesRepositoryReal(departureDao)
        ServiceLocator.departuresRepository = repository
        departuresRepositoryTests =
            DeparturesRepositoryTests(repository, context, true)
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
    fun testLoadErrMsg() {
        departuresRepositoryTests.testLoadErrMsg()
    }

    @Test
    fun getDeparturesLoadingInProgress() {
        departuresRepositoryTests.getDeparturesLoadingInProgress()
    }

    @Test
    fun getDepartures() {
        departuresRepositoryTests.getDepartures()
    }

    @Test
    fun toggleMagnifiedNormalView() {
        departuresRepositoryTests.toggleMagnifiedNormalView()
    }

    @Test
    fun deleteAllDepartures() {
        departuresRepositoryTests.deleteAllDepartures()
    }

}