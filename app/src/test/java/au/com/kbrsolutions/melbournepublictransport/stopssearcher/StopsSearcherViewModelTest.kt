package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.repository.StopsSearcherRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.getLiveDataValue
import au.com.kbrsolutions.melbournepublictransport.testutils.getOrAwaitValue
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This test will uzse Robolectric to get 'context'
 */
@RunWith(AndroidJUnit4::class)
//@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StopsSearcherViewModelTest {

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()

    // Subject under test
    private lateinit var stopsSearcherViewModel: StopsSearcherViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var stopsSearcherRepository: StopsSearcherRepositoryFake
    private lateinit var favoriteStopsRepositoryFake: FavoriteStopsRepositoryFake

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        stopsSearcherRepository = StopsSearcherRepositoryFake()
        favoriteStopsRepositoryFake = FavoriteStopsRepositoryFake()
    }

    @Test
    fun startLineAndStopsSearch_healthFalse_assertErrorMsg() {
        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        stopsSearcherViewModel = StopsSearcherViewModel(favoriteStopsRepositoryFake, stopsSearcherRepository, context)
        stopsSearcherViewModel.startLineAndStopsSearch("franks")
        val loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
        assertThat(loadErrMsgValue).isEqualTo(context.getString(R.string.ptv_is_not_available))
    }

    @Test
    fun startLineAndStopsSearch_healthTrue_assertErrorMessageNull() {
        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_only.json")
        stopsSearcherViewModel = StopsSearcherViewModel(favoriteStopsRepositoryFake, stopsSearcherRepository, context)
        stopsSearcherViewModel.startLineAndStopsSearch("franks")
        val loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
        assertThat(loadErrMsgValue).isEqualTo(null)
    }

//    @Test
    fun startLineAndStopsSearch_healthFalseTryAgainHealthTrue_assertErrortMessageNull_0() {
        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        stopsSearcherViewModel = StopsSearcherViewModel(favoriteStopsRepositoryFake, stopsSearcherRepository, context)
        stopsSearcherViewModel.startLineAndStopsSearch("franks")
        var loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
        assertThat(loadErrMsgValue).isEqualTo((context.getString(R.string.ptv_is_not_available)))

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_only.json")
//        stopsSearcherViewModel = StopsSearcherViewModel(repository, context)
        stopsSearcherViewModel.startLineAndStopsSearch("franks")
        loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
        assertThat(loadErrMsgValue).isEqualTo(null)
    }

    // below is too complicated for me to implement - may be later
//    @Test
    fun startLineAndStopsSearch_healthFalseTryAgainHealthTrue_assertErrortMessageNull() {
        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        stopsSearcherViewModel = StopsSearcherViewModel(favoriteStopsRepositoryFake, stopsSearcherRepository, context)

        // Create observer - no need for it to do anything!
        /*val observer = Observer<StopsSearcherViewModel> {}
        try {
            // Observe the LiveData forever
            stopsSearcherViewModel.loadErrMsg.observeForever(observer)

            stopsSearcherViewModel.startLineAndStopsSearch("franks")
            var loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
            println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
            assertThat(loadErrMsgValue).isEqualTo((context.getString(R.string.ptv_is_not_available)))
        } finally {

        }*/

        stopsSearcherViewModel.startLineAndStopsSearch("franks")

        val loadErrMsgValue = stopsSearcherViewModel.loadErrMsg.getOrAwaitValue()

//        var loadErrMsgValue = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue: $loadErrMsgValue} """)
        assertThat(loadErrMsgValue).isEqualTo((context.getString(R.string.ptv_is_not_available)))

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_only.json")
        stopsSearcherViewModel.startLineAndStopsSearch("franks")

        var loadErrMsgValue1 = stopsSearcherViewModel.loadErrMsg.getOrAwaitValue()

        // the getContentIfNotHandled() is defined in the Event class of the 'testing' code lab
//        val v = loadErrMsgValue1.getContentIfNotHandled()
//        val loadErrMsgValue1 = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue1: $loadErrMsgValue1} """)
        loadErrMsgValue1 = stopsSearcherViewModel.loadErrMsg.getOrAwaitValue()
//        val loadErrMsgValue1 = getLiveDataValue(stopsSearcherViewModel.loadErrMsg)
        println(G_P + """StopsSearcherViewModelTest - startLineAndStopsSearch - loadErrMsgValue1: $loadErrMsgValue1} """)
        assertThat(loadErrMsgValue1).isEqualTo(null)
    }
}