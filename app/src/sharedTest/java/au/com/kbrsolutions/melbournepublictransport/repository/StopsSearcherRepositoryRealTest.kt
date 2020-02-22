package au.com.kbrsolutions.melbournepublictransport.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.ServiceLocator
import au.com.kbrsolutions.melbournepublictransport.data.AppDatabase
import au.com.kbrsolutions.melbournepublictransport.data.LineStopDetailsDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StopsSearcherRepositoryRealTest {
    private lateinit var database: AppDatabase
    private lateinit var lineStopDetailsDao: LineStopDetailsDao
    private lateinit var repository: StopsSearcherRepositoryReal
    private lateinit var context: Context
    private lateinit var stopsSarcherRepositoryTests: StopsSarcherRepositoryTests

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
        lineStopDetailsDao = database.lineStopDetailsDao()
        repository = StopsSearcherRepositoryReal(lineStopDetailsDao)
        ServiceLocator.stopsSearcherRepository = repository
        stopsSarcherRepositoryTests =
            StopsSarcherRepositoryTests(repository, context, true)
    }

    @After
    @ExperimentalCoroutinesApi
    fun closeDb() {
        fun cleanupDb() = runBlockingTest {
            database.close()
            ServiceLocator.resetStopsSearcherRepository()
        }
    }

    @Test
    fun getStopsSearcherResults_responseHealthFalse() {
        stopsSarcherRepositoryTests.getStopsSearcherResults_responseHealthFalse()
    }

    @Test
    fun getStopsSearcherResults_find2StopsOnly() {
        stopsSarcherRepositoryTests.getStopsSearcherResults_find2StopsOnly()
    }

    @Test
    fun clearTable() {
        stopsSarcherRepositoryTests.clearTable()
    }

    @Test
    fun getLineStopDetails() {
        stopsSarcherRepositoryTests.getLineStopDetails()
    }

    @Test
    fun toggleMagnifiedView() {
        stopsSarcherRepositoryTests.toggleMagnifiedView()
    }

}