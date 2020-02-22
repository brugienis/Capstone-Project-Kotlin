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

class StopsSearcherRepositoryFakeTest {
    private lateinit var repository: StopsSearcherRepositoryFake
    private lateinit var context: Context
    private lateinit var stopsSarcherRepositoryTests: StopsSarcherRepositoryTests

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRepository() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = StopsSearcherRepositoryFake()
        ServiceLocator.stopsSearcherRepository = repository
        stopsSarcherRepositoryTests =
            StopsSarcherRepositoryTests(repository, context, false)
    }

    @After
    @ExperimentalCoroutinesApi
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetStopsSearcherRepository()
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