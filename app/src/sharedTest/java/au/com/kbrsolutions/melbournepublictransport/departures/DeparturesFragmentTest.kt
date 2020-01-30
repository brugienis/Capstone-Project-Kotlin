package au.com.kbrsolutions.melbournepublictransport.departures

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import au.com.kbrsolutions.melbournepublictransport.*
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.data.asDomainModel
import au.com.kbrsolutions.melbournepublictransport.data.helper.TestDataGenerator
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.repository.DeparturesRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.RecyclerViewMatcher
import au.com.kbrsolutions.melbournepublictransport.utilities.Misc
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

/**
 * Integration test for the Departures List screen.
 *
 * Based on the Code Lab's Android Test
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi

class DeparturesFragmentTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: DeparturesRepositoryFake
//    private val departuresList = TestDataGenerator.generateDataDeparturesList3Rows()
    private val departuresList = TestDataGenerator.generateDataDeparturesList1Row()

    @Before
    fun initRepository() {
        repository = DeparturesRepositoryFake().apply { setSimulatedDelayMillis(3_000) }
        repository.addDepartures(departuresList)
        ServiceLocator.departuresRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        repository.setSimulatedDelayMillis(0)
        ServiceLocator.resetDeparturesRepository()
    }

    /**
     * Set the value of the [favoriteStopsRequestedTimMillis] to the default value defined
     * in the DeparturesFragment. That will prevent unnecessary calls to the DeparturesViewModel's
     * loadDepartures(). The Departures screen will only show the data loaded in the initRepository
     * function annotated with @Before.
     *
     * The test below will fail if more than one row is found in the repository, because there will
     * be multiple rows matching 'departuresTopLayoutId'. In the future add code that will add
     * information that identify a specific row, e.g., by specifying some unique text in the row.
     *
     */
    @Test
    fun clickListViewRow_toggleMagnifiedView() {
        // Given a user in the home screen
        val favoriteStopsRequestedTimMillis = 0L
        val bundle = DeparturesFragmentArgs("1035", "Test location",
            favoriteStopsRequestedTimMillis).toBundle()
        val scenario = launchFragmentInContainer<DeparturesFragment>(bundle, R.style.AppTheme)
        scenario.onFragment {
        }

        // THEN - Verify 'magnified' layout section is not displayed on screen
        R.id.departuresTopLayoutId.checkIsDisplayed()

        // Click on the favoriteStopsTransportImageId
        R.id.departuresTransportImageId.performClick()

        // THEN - Verify 'magnified' layout section is displayed on screen
        R.id.departuresTopLayoutMagId.checkIsDisplayed()
        delayNextAction(2_000)
    }

    @Test
    fun verifyListViewItemsOrder() {
        val favoriteStopsRequestedTimMillis = 0L
        val bundle = DeparturesFragmentArgs("1035", "Test location",
            favoriteStopsRequestedTimMillis).toBundle()
        var derparturesList: List<DatabaseDeparture>? = null
        val rowsToLoadCnt = 1000
        val scenario =
            launchFragmentInContainer<DeparturesFragment>(bundle, R.style.AppTheme)
        scenario.onFragment {
            val context = it.context
            context?.let {
                // Make sure the sort order is 'by time' at the beginning of the test
                SharedPreferencesUtility.setSortDeparturesDataByTime(context, true)
                derparturesList = TestDataGenerator.getDataDeparturesListNRows(
                    rowsToLoadCnt,
                    context
                )
                repository.addDepartures(
                    (TestDataGenerator.getDataDeparturesListNRows(
                        rowsToLoadCnt,
                        context
                    ))
                )
            }
        }

        // Sort departures by time
        var departuresListSorted =
            Misc.sortDeparturesData(derparturesList!!.asDomainModel(), true)

        verifyDeparturesOrder(departuresListSorted)

        delayNextAction(3_000)

        // Sort departures by direction
        departuresListSorted =
            Misc.sortDeparturesData(derparturesList!!.asDomainModel(), false)

        // Simulate click on the 'Sort by direction' menu item
        scenario.onFragment {
            it.sortDepartureDataAndUpdateAdapter(false)
        }

        delayNextAction(3_000)

        verifyDeparturesOrder(departuresListSorted)

        delayNextAction(3_000)
    }

    private fun verifyDeparturesOrder(departuresListSorted: List<Departure>) {
        departuresListSorted.forEachIndexed { index, departure ->
            // First scroll to the position that needs to be matched
            onView(withId(R.id.departuresList))
                .perform(scrollToPosition<DeparturesAdapter.ViewHolderNormal>(index))

            // Validate that the row on the screen contains correct data
            onView(withRecyclerView(R.id.departuresList).atPosition(index))
                .check(matches(hasDescendant(withText(departuresListSorted[index].directionName))))
                .check(matches(hasDescendant(withText(departuresListSorted[index].departureTimeHourMinutes))))
        }
    }

    // Convenience helper
    private fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

    private val ignoreDelay: Boolean = true

    private fun delayNextAction(millis: Int) {
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