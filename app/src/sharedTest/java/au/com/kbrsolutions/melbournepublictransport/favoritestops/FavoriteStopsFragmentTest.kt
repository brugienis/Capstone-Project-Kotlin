package au.com.kbrsolutions.melbournepublictransport.favoritestops

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import au.com.kbrsolutions.melbournepublictransport.*
import au.com.kbrsolutions.melbournepublictransport.R.id
import au.com.kbrsolutions.melbournepublictransport.activities.MainActivity
import au.com.kbrsolutions.melbournepublictransport.domain.FavoriteStop
import au.com.kbrsolutions.melbournepublictransport.repository.FavoriteStopsRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.annotation.LooperMode
import org.robolectric.annotation.TextLayoutMode

/**
 * Integration test for the Favorite Stops List screen.
 *
 * Copied from the Code Labs' Android Test
 */
// TODO - Use FragmentScenario, see: https://github.com/android/android-test/issues/291
@RunWith(AndroidJUnit4::class)
@MediumTest
@LooperMode(LooperMode.Mode.PAUSED)
@TextLayoutMode(TextLayoutMode.Mode.REALISTIC)
@ExperimentalCoroutinesApi
class FavoriteStopsFragmentTest {

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FavoriteStopsRepositoryFake
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
        repository = FavoriteStopsRepositoryFake().apply { setSimulatedDelayMillis(3_000) }
        repository.addFavoriteStops(favoriteStop)
        ServiceLocator.favoriteStopsRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        repository.setSimulatedDelayMillis(0)
        ServiceLocator.resetFavoriteStopsRepository()
    }

    @Test
    fun clickShowDepartures_navigateToDeparturesFragment() {
        // Given a user in the home screen
        val scenario = launchFragmentInContainer<FavoriteStopsFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        var navigateToNextDeparturesRequestMillis = 0L
        scenario.onFragment {
            //            it.dataSource = sleepDao
            Navigation.setViewNavController(it.view!!, navController)
            navigateToNextDeparturesRequestMillis = it.navigateToNextDeparturesRequestMillis
        }

        /*
         Click favoriteStopsTransportImageId icon to show magnified layout. Had to do it because
         when we click on that icon in the normal layout, we got the below exception.
         Caused by: java.lang.RuntimeException: Action will not be performed because the target
         view does not match one or more of the following constraints:
            at least 90 percent of the view's area is displayed to the user.
        */
        id.favoriteStopsTransportImageId.performClick()

        id.favoriteStopsDeparturesImageMagId.performClick()

        // Then verify that we navigate to the NextDeparture screen
        verify(navController).navigate(
            FavoriteStopsFragmentDirections.actionFavoriteStopsFragmentToNextDeparturesFragment(
                1000,
                "Embarcadero Station",
                navigateToNextDeparturesRequestMillis
//                System.currentTimeMillis()
            )
        )
    }

    @Test
    fun clickListViewRow_toggleMagnifiedView() {
        // Given a user in the home screen
        launchFragmentInContainer<FavoriteStopsFragment>(Bundle(), R.style.AppTheme)

        // THEN - Verify 'magnified' layout section is not displayed on screen
        id.favoriteStopsIconsMagId.checkIsNotDisplayed()

        // Click on the favoriteStopsTransportImageId
        id.favoriteStopsTransportImageId.performClick()

        // THEN - Verify 'magnified' layout section is displayed on screen
        id.favoriteStopsIconsMagId.checkIsDisplayed()
    }

    /**
     *
     * .   .   .
    +---->RecyclerView{id=2131296420, res-name=favoriteStopsList, visibility=VISIBLE, width=1038, height=1689, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=false, is-selected=false, layout-params=androidx.constraintlayout.widget.ConstraintLayout$LayoutParams@46d1d1f, tag=null, root-is-layout-requested=false, has-input-connection=false, x=21.0, y=21.0, child-count=3}
    |
    +----->LinearLayout{id=-1, visibility=VISIBLE, width=1038, height=168, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=false, is-selected=false, layout-params=androidx.recyclerview.widget.RecyclerView$LayoutParams@3d06c3b, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, child-count=3}
    |
    +------>ImageView{id=2131296425, res-name=favoriteStopsTransportImageId, desc=Train, visibility=VISIBLE, width=126, height=126, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@15a3f58, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=21.0}
    |
    +------>LinearLayout{id=-1, visibility=VISIBLE, width=709, height=63, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@edff904, tag=null, root-is-layout-requested=false, has-input-connection=false, x=126.0, y=52.0, child-count=3}
    |
    +------->TextView{id=2131296421, res-name=favoriteStopsLocationNameId, visibility=VISIBLE, width=316, height=63, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@546eed, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, text=Walnut Creek 1, input-type=0, ime-target=false, has-links=false}
    |
    +------->TextView{id=2131296424, res-name=favoriteStopsSuburbId, visibility=GONE, width=0, height=0, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=true, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@60da122, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, text=Walnut Creek, input-type=0, ime-target=false, has-links=false}
    |
    +------->LinearLayout{id=2131296419, res-name=favoriteStopsIconsMagId, visibility=GONE, width=0, height=0, has-focus=false, has-focusable=false, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@9d8c0f, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0, child-count=4}
    |
    +-------->ImageView{id=2131296415, res-name=favoriteStopsDeparturesImageMagId, desc=Show databaseDepartures, visibility=VISIBLE, width=0, height=0, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=true, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@65b649c, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0}
    |
    +-------->ImageView{id=2131296423, res-name=favoriteStopsStopFacilityId, desc=Show stop facility, visibility=VISIBLE, width=0, height=0, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=true, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@a398ea5, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0}
    |
    +-------->ImageView{id=2131296422, res-name=favoriteStopsMapImageMagId, desc=Show stop on map, visibility=VISIBLE, width=0, height=0, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=true, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@c96327a, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0}
    |
    +-------->ImageView{id=2131296417, res-name=favoriteStopsGarbageImageMagId, desc=Remove stop from favorites, visibility=VISIBLE, width=0, height=0, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=true, is-enabled=true, is-focused=false, is-focusable=true, is-layout-requested=true, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@3b0da2b, tag=null, root-is-layout-requested=false, has-input-connection=false, x=0.0, y=0.0}
    |
    +------>LinearLayout{id=2131296418, res-name=favoriteStopsIconsId, visibility=VISIBLE, width=203, height=126, has-focus=false, has-focusable=true, has-window-focus=true, is-clickable=false, is-enabled=true, is-focused=false, is-focusable=false, is-layout-requested=false, is-selected=false, layout-params=android.widget.LinearLayout$LayoutParams@fd97021, tag=null, root-is-layout-requested=false, has-input-connection=false, x=835.0, y=21.0, child-count=1}
    |
     */
    @Test
    fun magnifiedRowDeleted_toggleRowThatUsedToBeAfterToDeleted() {
        val walnutCreek = "Walnut Creek"
        val lafayette = "Lafayette"
        val sanFrancisco = "San Francisco"
        val stopsList = listOf(
            FavoriteStop(
                0,
                0,
                1000,
                walnutCreek,
                "Walnut Creek",
                1.1,
                2.2,
                false
            ),
            FavoriteStop(
                1,
                0,
                2000,
                lafayette,
                "Lafayette",
                1.1,
                2.2,
                false
            ),
            FavoriteStop(
                2,
                0,
                3000,
                sanFrancisco,
                "San Francisco",
                1.1,
                2.2,
                false
            ))
        repository = FavoriteStopsRepositoryFake()
        repository.addFavoriteStops(*stopsList.toTypedArray())
        ServiceLocator.favoriteStopsRepository = repository
        // Given a user in the home screen
        launchFragmentInContainer<FavoriteStopsFragment>(Bundle(), R.style.AppTheme)

        // Magnify row
        onView(
            Matchers.allOf(
                withId(id.favoriteStopsLocationNameId),
                withText(lafayette))
        ).perform(ViewActions.click())

        // Delete row
        clickOnViewWithIdWhoseParentsParentTextViewContainsText(
            lafayette,
            id.favoriteStopsGarbageImageMagId)

        // THEN - Verify 'lafayette' row does not exists

        onView(withText(lafayette)).check(doesNotExist())

        // The other 2 rows are still displayed
        onView(
            Matchers.allOf(
                withId(id.favoriteStopsLocationNameId),
                withText(walnutCreek))
        ).check(matches(isDisplayed()))

        onView(
            Matchers.allOf(
                withId(id.favoriteStopsLocationNameId),
                withText(sanFrancisco))
        ).check(matches(isDisplayed()))

        // Magnify another row - used to throw exception
        onView(
            Matchers.allOf(
                withId(id.favoriteStopsLocationNameId),
                withText(sanFrancisco))
        ).perform(ViewActions.click())
    }

    @Test
    fun deleteFavoriteStop() {
        launchFragmentInContainer<FavoriteStopsFragment>(Bundle(), R.style.AppTheme)

        // Magnify row - click on the favoriteStopsTransportImageId
        id.favoriteStopsTransportImageId.performClick()

        // Delete row
        clickOnViewWithIdWhoseParentsParentTextViewContainsText(
            embarcaderoLocationName,
            id.favoriteStopsGarbageImageMagId)

        // THEN - Verify 'lafayette' row does not exists

        onView(withText(embarcaderoLocationName)).check(doesNotExist())

        // Verify that view containing text with instructions on how to add 'favorite' stops
        // is visible
        id.emptyFavoriteStopsView.checkIsDisplayed()
    }

    @Test
    fun deleteAllFavoriteStops() {
        ActivityScenario.launch(MainActivity::class.java)

        id.favStops_removeAllFavoriteStops.performMenuItemClick("Delete all favorite stops")

        // Verify that view containing text with instructions on how to add 'favorite' stops
        // is visible

        id.emptyFavoriteStopsView.checkIsDisplayed()
    }

    @Test
    fun clickStopsSearcherMenuItem_navigateToStopsSearcherFragment() {
        // Given a user in the home screen
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        id.favStops_goToStopsSearcherFragment.performMenuItemClick("Add favorite stop")

        // Verify StopsSearcherFragment's stopsSearchText EditText is displayed on screen
        id.stopsSearchText.checkIsDisplayed()
    }

    @Test
    fun verifyMenuItems() {
        // Given a user in the home screen
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Verify all 'favorite stops' menu items are available
        id.favStops_goToStopsSearcherFragment.checkMenuItemIsDisplayed("Add favorite stop")
        id.favStops_removeAllFavoriteStops.checkMenuItemIsDisplayed("Delete all favorite stops")
        id.favStops_trainStopsNearby.checkMenuItemIsDisplayed("Train stops nearby")
        id.favStops_stopsNearby.checkMenuItemIsDisplayed("Stops nearby")
        id.favStops_disruptions.checkMenuItemIsDisplayed("Disruptions")
    }

    private val ignoreDelay: Boolean = true

    private fun delay(millis: Int) {
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

    /*@Test
    fun mockSimpleTests() {
        val myClass = mock(MockitoTests::class.java)

        myClass.addLong("stringy1", "stringy2", 111L)


//        verify(myClass).addLong("stringy", "stringy9", ArgumentMatchers.anyLong())
        verify(myClass).addLong("stringy", "stringy9", 222L)
    }

    @Test
    fun mockTest() {
        val navController = mock(NavController::class.java)


        // Then verify that we navigate to the add screen
        navController.navigate(
            FavoriteStopsFragmentDirections.actionFavoriteStopsFragmentToNextDeparturesFragment("1035", "Carrum", 111L)
        )
        verify(navController).navigate(
            FavoriteStopsFragmentDirections.actionFavoriteStopsFragmentToNextDeparturesFragment(
                ArgumentMatchers.eq("1035"), eq("Carrum"), ArgumentMatchers.anyLong())
        )
    }*/
}
