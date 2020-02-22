package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.content.res.Resources
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import au.com.kbrsolutions.melbournepublictransport.MainCoroutineRule
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.ServiceLocator
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.performTypeText
import au.com.kbrsolutions.melbournepublictransport.repository.StopsSearcherRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.DataBindingIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

// fixLater: Jan 22, 2020 - research it`
/**
 * Use this class to research use of the EspressoIdlingResource and DataBindingIdlingResource.
 *
 * I do not see any impact of EspressoIdlingResource in tests using coroutines. So far.
 *
 * INVESTIGATE.
 */
class StopsSearcherFragmentTest_reserch_espressoIdlingResource {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    private lateinit var stopsSearcherRepository: StopsSearcherRepositoryFake
    val embarcaderoStopId = 1000
    val embarcaderoLocationName = "Embarcadero Station"

    @Before
    fun initRepository() {
        val favoriteStop = FavoriteStop(
            0,
            0,
            embarcaderoStopId,
            embarcaderoLocationName,
            "San Francisco",
            1.1,
            2.2,
            false
        )
        stopsSearcherRepository = StopsSearcherRepositoryFake().apply {
            setSimulatedDelayMillis(3_000)
        }
        ServiceLocator.stopsSearcherRepository = stopsSearcherRepository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetFavoriteStopsRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue() {
        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue - start""")

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        // Given a user in the home screen
        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
            resources = it.resources
        }

        // Initiate the first search request - response will contain 'health' flag false

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("01")
        // Click on the 'action search button' on the keyboard - magnifying glass icon
        Espresso.onView(ViewMatchers.withId(R.id.stopsSearchText))
            .perform(ViewActions.pressImeActionButton())
        delayNextStep(5_000)

        // Initiate the second search request - response will contain 'health' flag true

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_only.json")

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("02")

        // Click on the 'action search button' on the keyboard - magnifying glass icon
        Espresso.onView(ViewMatchers.withId(R.id.stopsSearchText))
            .perform(ViewActions.pressImeActionButton())

//        delayNextStep(10_000)

        // Verify that Snackbar does not appear.
        Espresso.onView(ViewMatchers.withId(R.id.snackbar_text)).check(ViewAssertions.doesNotExist())

        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue - end""")
    }

    private var ignoreDelay: Boolean = true

    private fun delayNextStep(millis: Int) {
        if (ignoreDelay) {
            return
        }
        try {
            //            Log.v(TAG, "delay - sleep start");
            Thread.sleep(millis.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}