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

class DeparturesRepositoryFakeTest {
    private lateinit var repository: DeparturesRepositoryFake
    private lateinit var context: Context
    private lateinit var departuresRepositoryTests: DeparturesRepositoryTests

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun initRepository() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = DeparturesRepositoryFake()
        ServiceLocator.departuresRepository = repository
        departuresRepositoryTests =
            DeparturesRepositoryTests(repository, context, false)
    }

    @After
    @ExperimentalCoroutinesApi
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetStopsSearcherRepository()
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