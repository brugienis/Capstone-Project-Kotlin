package au.com.kbrsolutions.melbournepublictransport.activities

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import au.com.kbrsolutions.melbournepublictransport.*
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.DataBindingIdlingResource
import au.com.kbrsolutions.melbournepublictransport.testutils.monitorActivity
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class MainActivityTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FavoriteStopsRepositoryFake

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    val embarcaderoStopId = "Embarcadero"
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
        repository = FavoriteStopsRepositoryFake().apply { setSimulatedDelayMillis(3_000) }
        repository.addFavoriteStops(favoriteStop)
        ServiceLocator.favoriteStopsRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        repository.setSimulatedDelayMillis(0)
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
    fun verifyDeparturesMenuItems() {
        // Given a user in the home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        var resources: Resources? = null
        activityScenario.onActivity {
            SharedPreferencesUtility.setSortDeparturesDataByTime(it!!, true)
            resources = it.resources
        }

        // Row is not 'magnified' - click on the 'departures' icon
        R.id.favoriteStopsDeparturesImageId.performClick()

        // We should see now the 'departures' screen.
        // Verify that all 'departures' menu items are available
        R.id.directions_menu_refresh_data.checkMenuItemIsDisplayed("Refresh screen")

        // Departures list is showing
        R.id.departuresList.checkIsDisplayed()

        // Click on the 'Sort by direction' menu item
        R.id.directions_menu_sort_by_direction.performMenuItemClick("Sort by direct")

        // Click on the 'Sort by time' menu item..
        R.id.directions_menu_sort_by_direction.performMenuItemClick(resources!!.getString(R.string.action_sort_by_time))
        delayAction(2_000)
    }

    @Test
    fun verifyActivityAndFavoriteStopsFragmentMenuItems() {
        // Given a user in the home screen
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        delayAction(3_000)
        println(G_P + """MainActivityTest - verifyActivityAndFavoriteStopsFragmentMenuItems - start""")
        // Verify all 'favorite stops' menu items are available
        R.id.favStops_goToStopsSearcherFragment.checkMenuItemIsDisplayed("Add favorite stop")
        println(G_P + """MainActivityTest - verifyActivityAndFavoriteStopsFragmentMenuItems - after check Add favorite stop""")
        R.id.favStops_removeAllFavoriteStops.checkMenuItemIsDisplayed("Delete all favorite stops")
        R.id.favStops_trainStopsNearby.checkMenuItemIsDisplayed("Train stops nearby")
        R.id.favStops_stopsNearby.checkMenuItemIsDisplayed("Stops nearby")
        R.id.favStops_disruptions.checkMenuItemIsDisplayed("Disruptions")
    }

    private val ignoreDelay: Boolean = false

    private fun delayAction(millis: Int) {
        if (ignoreDelay) {
            return
        }
        try {
            //            Log.v(TAG, "delayAction - sleep start");
            Thread.sleep(millis.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}
