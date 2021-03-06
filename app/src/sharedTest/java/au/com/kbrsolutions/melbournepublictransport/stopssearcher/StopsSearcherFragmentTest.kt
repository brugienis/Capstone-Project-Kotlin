package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.util.Log
import android.view.KeyEvent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.pressKey
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import au.com.kbrsolutions.melbournepublictransport.*
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.repository.StopsSearcherRepositoryFake
import au.com.kbrsolutions.melbournepublictransport.testutils.DataBindingIdlingResource
import au.com.kbrsolutions.melbournepublictransport.testutils.monitorFragment
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

/**
 * Integration test for the Stops Searcher screen.
 *
 * Based on the Code Lab's Android Test
 */
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi

class StopsSearcherFragmentTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // An Idling Resource that waits for Data Binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()
    private val delayMillis = 2_500L

    private lateinit var favoriteStopsRepository: FavoriteStopsRepositoryFake
    private lateinit var stopsSearcherRepository: StopsSearcherRepositoryFake
    private val embarcaderoStopId = 1000
    private val embarcaderoLocationName = "Embarcadero Station"

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
        favoriteStopsRepository = FavoriteStopsRepositoryFake().apply { setSimulatedDelayMillis(delayMillis) }
        favoriteStopsRepository.addFavoriteStops(favoriteStop)
        ServiceLocator.favoriteStopsRepository = favoriteStopsRepository

        stopsSearcherRepository = StopsSearcherRepositoryFake().apply {
            setSimulatedDelayMillis(delayMillis)
        }
        ServiceLocator.stopsSearcherRepository = stopsSearcherRepository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        favoriteStopsRepository.setSimulatedDelayMillis(0)
        ServiceLocator.resetFavoriteStopsRepository()
        ServiceLocator.resetStopsSearcherRepository()
    }

    /**
     * Idling resources tell Espresso that the app is idle or busy. This is needed when operations
     * are not scheduled in the main Looper (for example when executed on a different thread).
     */
    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
//        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    /**
     * Unregister your Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
//        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun enterSearchText_responseHealthFalse_showErrMsg() {
        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg - enterSearchText_responseHealthFalse_showErrMsg start""")
        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        dataBindingIdlingResource.monitorFragment(scenario)
        scenario.onFragment {
//            resources = it.resources
        }

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("bl")

        // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        // Verify the stopsSearchValidationMsg contains correct error message
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchValidationMsg),
                ViewMatchers.withText(R.string.ptv_is_not_available)
            )
        ).checkIsDisplayed()

        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg - enterSearchText_responseHealthFalse_showErrMsg end""")
    }

    @Test
    fun enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue() {
        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue - start""")

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_health_false.json")
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        // Initiate the first search request - response will contain 'health' flag 'false'

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("01")
        // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        /* see EspressoShortcuts.showIdlingResourcesDetails(...) for more information */
        showIdlingResourcesDetails("enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue")

        // Verify the stopsSearchValidationMsg contains correct error message
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchValidationMsg),
                ViewMatchers.withText(R.string.ptv_is_not_available)
            )
        ).checkIsDisplayed()

        // Initiate the second search request - response will contain 'health' flag 'true'

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_2_stops_only.json")

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("02")

        // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        // Verify the stopsSearchValidationMsg does not contains correct error message
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchValidationMsg),
                ViewMatchers.withText(R.string.ptv_is_not_available)
            ))
            .check(ViewAssertions.doesNotExist())

        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue - end""")
    }

    @Test
    fun enterSearchTextWithInvalidCharacter_shouldShowValidationText() {
//        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_no_stops_routes_outlets.json")
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        // Initiate the first search request - response will contain 'health' flag 'false'

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("Dddddd`")
        // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        delayNextStep(5_000)
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchValidationMsg),
                ViewMatchers.withText(R.string.invalid_search_text)
            )
        ).checkIsDisplayed()

        onView(
//            Matchers.allOf(
                withId(R.id.searchInstructionsView)
//            )
        ).checkIsNotDisplayed()
    }

    @Test
    fun enterSearchTextThatWillReturnNothingFoundResponse_shouldShowEmptyResultsText() {
        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchTextThatWillReturnNothingFoundResponse_shouldShowEmptyResultsText - start""")

        stopsSearcherRepository.setDebuggingJsonStringFile("stops_searcher_no_stops_routes_outlets.json")
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        // Type some search text - less than 3 characters, so the automatic search will not start
        R.id.stopsSearchText.performTypeText("DD")

        // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())
        delayNextStep(5_000)

        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchValidationMsg),
                ViewMatchers.withText(R.string.empty_search_results)
            ))
            .checkIsDisplayed()

        onView(withId(R.id.searchInstructionsView))
            .checkIsNotDisplayed()
        delayNextStep(5_000)

        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchTextThatWillReturnNothingFoundResponse_shouldShowEmptyResultsText - end""")
    }

    // fixLater: Feb 20, 2020 - add test with favoriteStopsArray in the bundle contains a stopId.
    // fixLater: Feb 20, 2020 - Verify that this stopId is excluded from the search results.
    @Test
    fun enterSearchText_clickOnClearTextIcon() {
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        // Verify initial hint text
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        // Type some search text
        R.id.stopsSearchText.performTypeText("bla bla")

        delayNextStep(3_000)

        // Click on the 'clear text icon' to clear the 'searchText' EditText field
        onView(
            Matchers.allOf(
                withId(R.id.text_input_end_icon),
                ViewMatchers.withContentDescription("Clear text")
            )
        ).perform(ViewActions.click())

        delayNextStep(3_000)

        // Verify that 'search text' field is empty
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText("")
            )
        ).checkIsDisplayed()

        delayNextStep(3_000)

        // Verify that hint text did not changed
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

    }

    @Test
    fun enterSearchText_clickOnStartSearchKey() {
        // Given a user in the home screen
//        var resources: Resources? = null
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        // Verify initial hint text
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        // Type search text - type 2 characters to prevent automatic search start
        val textToType = "bl"
        R.id.stopsSearchText.performTypeText(textToType)
        delayNextStep(3_000)

//      // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        // Verify that hint text changed to 'Searching'
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.searching_in_progress)
            )
        ).checkIsDisplayed()

        // Verify that the 'search text' still shows the text that was typed before
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText(textToType)
            )
        ).checkIsDisplayed()
    }

    // fixLater: Mar 10, 2020 - document usage of EspressoIdlingResource. Extract lines from the 'logcat'.
    @Test
    fun enterSearchText_waitOneSec() {
        // Given a user in the home screen
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
//        val scenario =
        launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)

        // Verify initial hint text
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        // Type search text
        val textToType = "bla bla"
        Log.v(G_P + "StopsSearcherFragmentTest", """enterSearchText_waitOneSec - before performTypeText""")
        R.id.stopsSearchText.performTypeText(textToType)

        // Search should start one second after the last character was typed

        // Verify that hint text changed to 'Searching'
        onView(
            Matchers.allOf(
//                withId(R.id.stopsSearchText)
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.searching_in_progress)
            )
        ).checkIsDisplayed()

        // Verify that the 'search text' still shows the text that was typed before
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText(textToType)
            )
        ).checkIsDisplayed()
    }

    /*
        Type 2 characters - automatic search will not start
     */
    @Test
    fun enterSearchText2Chars_deleteAllTypedCharactersOneByOne() {
        // Given a user in the home screen
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
//        val scenario =
        launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)

        // Verify initial hint text
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        // Type search text
        val textToType = "bl"
        R.id.stopsSearchText.performTypeText(textToType)

        repeat (textToType.length) {
            onView(  withId(R.id.stopsSearchText)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        }

        delayNextStep(3_000)

        // Verify that hint text changed to 'Enter search text'
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        delayNextStep(3_000)

        // Verify that the 'stopsSearchText' is empty
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText("")
            )
        ).checkIsDisplayed()

        delayNextStep(3_000)
    }

    /*
        Type 3 characters - automatic search will start. Touch the stopsSearchText to show the
        keyboard and delete all typed characters.
     */
    @Test
    fun enterSearchText3Chars_deleteAllTypedCharactersOneByOne() {
        // Given a user in the home screen
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
//        val scenario =
        launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)

        // Verify initial hint text
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.enter_search_text)
            )
        ).checkIsDisplayed()

        // Type search text
        val textToType = "bla"
        R.id.stopsSearchText.performTypeText(textToType)

        // By now the automatic search should start

        // Touch the stopsSearchText - it should cancel the current search and show keyboard
        R.id.stopsSearchText.performClick()

        repeat (textToType.length) {
            onView(  withId(R.id.stopsSearchText)).perform(pressKey(KeyEvent.KEYCODE_DEL))
        }

        // Now we should see the 'Search cancelled - enter search text' hint
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.search_cancelled_enter_search_text)
            )
        ).checkIsDisplayed()

        // Verify that the 'stopsSearchText' is empty
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText("")
            )
        ).checkIsDisplayed()
    }

    @Test
    fun enter3CharsSearchTextAndWait1Second_touchSearchTextToCancelSearchInProgress() {
        // Given a user in the home screen
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
//        val scenario =
        launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)

        // Type search text
        R.id.stopsSearchText.performTypeText("bla bla")

        // Touch the stopsSearchText - it should cancel the current search
        R.id.stopsSearchText.performClick()

        // Verify that hint text changed to: 'Search cancelled - enter search text'
        onView(

            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.search_cancelled_enter_search_text)
            )
        ).checkIsDisplayed()
    }

    @Test
    fun enterSearchTextAndStartSearch_touchSearchTextToCancelSearchInProgress() {
        // Given a user in the home screen
        val favoriteStopsArray = StopsSearcherFragmentArgs(IntArray(0)).toBundle()
        val scenario =
            launchFragmentInContainer<StopsSearcherFragment>(favoriteStopsArray, R.style.AppTheme)
        scenario.onFragment {
//            resources = it.resources
        }

        typeTextStartSearchThanTouchStopsSearchText_shouldCancelCurrSearch("bla bla")

        // Click on the 'clear text icon' to clear the 'searchText' EditText field
        onView(
            Matchers.allOf(
                withId(R.id.text_input_end_icon),
                ViewMatchers.withContentDescription("Clear text")
            )
        ).perform(ViewActions.click())

        typeTextStartSearchThanTouchStopsSearchText_shouldCancelCurrSearch("bla bla hola")
    }

    private fun typeTextStartSearchThanTouchStopsSearchText_shouldCancelCurrSearch(textToType: String) {
        Log.v(G_P + "StopsSearcherFragmentTest", """typeTextStartSearchTouchStopsSearchText_shouldCancelCurrSearch - start""")

        // Type search text
        R.id.stopsSearchText.performTypeText(textToType)
        delayNextStep(3_000)

//      // Click on the 'action search button' on the keyboard - magnifying glass icon
        onView(withId(R.id.stopsSearchText))
            .perform(pressImeActionButton())

        delayNextStep(3_000)
        // Touch the stopsSearchText - it should cancel the current search
        R.id.stopsSearchText.performClick()

        delayNextStep(3_000)

        // Verify that the 'search text' still shows the text that was typed before
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withText(textToType)
            )
        ).checkIsDisplayed()

        delayNextStep(3_000)

        // Verify that hint text changed to: 'Search cancelled - enter search text'
        onView(
            Matchers.allOf(
                withId(R.id.stopsSearchText),
                ViewMatchers.withHint(R.string.search_cancelled_enter_search_text)
            )
        ).checkIsDisplayed()
    }

    private var ignoreDelay: Boolean = false

    private fun delayNextStep(millis: Long) {
        if (ignoreDelay) {
            return
        }
        try {
            //            Log.v(TAG, "delay - sleep start");
            Thread.sleep(millis)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}