package au.com.kbrsolutions.melbournepublictransport

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.AdapterView
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import java.lang.Thread.sleep

/*
    check package androidx.test.espresso.matcher - HasSiblingMatcher
    (hasSibling(ViewMatchers.withText("some text"))), etc.
    public final class ViewMatchers.

    package org.hamcrest
    public class Matchers
*/

fun ViewInteraction.performClick(): ViewInteraction = perform(ViewActions.click())

fun Int.matchView(): ViewInteraction = Espresso.onView(ViewMatchers.withId(this))

fun Int.performClick(): ViewInteraction = matchView().performClick()

fun ViewInteraction.performTypeText(stringToBeTyped: String): ViewInteraction =
    perform(ViewActions.typeText(stringToBeTyped))

fun Int.performTypeText(stringToBeTyped: String): ViewInteraction =
    matchView().performTypeText(stringToBeTyped)

//-------------------------------------------------------------------------------

fun ViewInteraction.checkIsDisplayed(): ViewInteraction =
    check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

// fixLater: Mar 12, 2020 - remove below
/*fun ViewInteraction.checkIsDisplayedDebug(): ViewInteraction {
    Log.v(G_P + "EspressoShortcuts", """checkIsDisplayedDebug - countingIdlingResource counter: ${EspressoIdlingResource.countingIdlingResource.getCounter()}""")
    while (EspressoIdlingResource.countingIdlingResource.getCounter().get() > 0) {
        sleep(1_000)
        Log.v(G_P + "EspressoShortcuts", """checkIsDisplayedDebug - waking counter: ${EspressoIdlingResource.countingIdlingResource.getCounter()}""")
    }
    return check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}*/

fun ViewInteraction.checkIsNotDisplayed(): ViewInteraction =
    check(ViewAssertions.matches(not(isDisplayed())))

/**
 * We have now one test case
 *
 *     enterSearchText_responseHealthFalse_showErrMsg_startSearchAgainWithResponseHealthTrue
 *
 * where the 'verify' step starts when the
 *
 *     EspressoIdlingResource.countingIdlingResource.getCounter()
 *
 * is not 0 - weird. The Espresso framework should take care of that.
 *
 * This function blocks the next Espresso's step until the counter is zero.
 */
fun showIdlingResourcesDetails(source: String) {
    Log.v(G_P + "EspressoShortcuts", """showIdlingResourcesDetails - countingIdlingResource counter: ${EspressoIdlingResource.countingIdlingResource.getCounter()} source: $source""")
    while (EspressoIdlingResource.countingIdlingResource.getCounter().get() > 0) {
        sleep(1_000)
        Log.v(G_P + "EspressoShortcuts", """showIdlingRecourcesDetails - waking countingIdlingResource counter: ${EspressoIdlingResource.countingIdlingResource.getCounter()}""")
    }
}

//-------------------------------------------------------------------------------

/**
 * Verifies that the view is displayed.
 *
 * Instead of:
 *
 *     onView(withId(R.id.favoriteStopsIconsMagId)).check(matches(isDisplayed()))
 *
 *  use:
 *
 *     R.id.favoriteStopsIconsMagId.checkIsDisplayed()
 *
 */
fun Int.checkIsDisplayed(): ViewInteraction = matchView().checkIsDisplayed()

/**
 * Verifies that the view is displayed.
 *
 * Instead of:
 *
 *     onView(withId(R.id.favoriteStopsIconsMagId)).check(matches(not(isDisplayed())))
 *
 *  use:
 *
 *     R.id.favoriteStopsIconsMagId.checkIsNotDisplayed()
 *
 */
fun Int.checkIsNotDisplayed(): ViewInteraction = matchView().checkIsNotDisplayed()

//-------------------------------------------------------------------------------

fun validateActionbarTitle(expectedTitle: String) {
    val activityTitleTextView0 = Espresso.onView(
        Matchers.allOf(
            childTextViewAtPosition(
                ViewMatchers.withId(R.id.toolbar),
                expectedTitle,
                0),
            ViewMatchers.isDisplayed()))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    activityTitleTextView0.check(ViewAssertions.matches(ViewMatchers.withText(expectedTitle)))
}

//-------------------------------------------------------------------------------


fun childTextViewAtPosition(
    parentMatcher: Matcher<View>,
    expectedText: String,
    position: Int): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        var rowFound = false
        override fun describeTo(description: Description) {
            description.appendText("""Child at position: $position
                    | with expected text: $expectedText in parent """.trimMargin())
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            if (view !is TextView) {
                return false
            }
            val parent = view.parent

            /*if (parent is ViewGroup && parent.id == R.id.toolbar) {
                for (pos in 0.. parent.childCount) {
                    Log.v("CreateTestFolderTest", """childTextViewAtPosition.matchesSafely -
                            |parent:   $parent
                            |pos:      $pos
                            |child:    ${parent.getChildAt(pos)}
                            |""".trimMargin())
                }
            }*/
            if (parent is ViewGroup &&
                parentMatcher.matches(parent) &&
                view == parent.getChildAt(position) &&
                view.text.toString() == expectedText) {
                return true
            }
            return false

        }
    }
}

//-------------------------------------------------------------------------------

/**
 *  Perform click on a menu item.
 *
 *  Instead of:
 *
 *    // Open 'overflow action bar' in menu
 *    Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
 *    // Click 'delete all favorite stops' in menu
 *    onView(withText("Delete all favorite stops")).perform(ViewActions.click())
 *
 *  use:
 *
 *    R.id.removeAllFavoriteStops.performMenuItemClick("Delete all favorite stops")
 *
 */
var isMenuItemDisplayed = false
fun Int.performMenuItemClick(menuItemText: String) {
    isMenuItemDisplayed = true
    Espresso.onView(ViewMatchers.withId(this))
        .withFailureHandler { error, viewMatcher -> isMenuItemDisplayed = false }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        .perform(ViewActions.click())

    /* If the 'sMenuItemDisplayed' was not found in the ActionBar, try to find it in the      */
    /* overflow view, and clicked on it.                                                      */
    Log.v("EsspressoShortcuts", """performMenuItemClick (id)
        |id:                  ${this}
        |menuItemText:        ${menuItemText}
        |isMenuItemDisplayed: $isMenuItemDisplayed
    """.trimMargin())

    if (!isMenuItemDisplayed) {
        val overflowViewId = R.id.title
        try {
            Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        } catch (e: Exception) {
            //This is normal. Maybe we don't have overflow menu.
        }

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(overflowViewId),
                withTextStartWithString(menuItemText)
            ))
            .perform(ViewActions.click())
    }
}

fun Int.checkMenuItemIsDisplayed(menuItemText: String) {
    isMenuItemDisplayed = true
    Espresso.onView(ViewMatchers.withId(this))
        .withFailureHandler { error, viewMatcher -> isMenuItemDisplayed = false }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        .checkIsDisplayed()

    /* If the 'sMenuItemDisplayed' was not found in the ActionBar, try to find it in the      */
    /* overflow view, and clicked on it.                                                      */
    Log.v("EsspressoShortcuts", """checkMenuItemIsDisplayed (id)
        |id:                  ${this}
        |menuItemText:        ${menuItemText}
        |isMenuItemDisplayed: $isMenuItemDisplayed
    """.trimMargin())

    if (!isMenuItemDisplayed) {
        val overflowViewId = R.id.title
        try {
            Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        } catch (e: Exception) {
            //This is normal. Maybe we don't have overflow menu.
        }

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(overflowViewId),
                withTextStartWithString(menuItemText)
            ))
            .checkIsDisplayed()
    }
}

fun Int.checkMenuItemIsNotDisplayed(menuItemText: String) {
    isMenuItemDisplayed = true
    Espresso.onView(ViewMatchers.withId(this))
        .withFailureHandler { error, viewMatcher -> isMenuItemDisplayed = false }
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        .checkIsNotDisplayed()

    /* If the 'sMenuItemDisplayed' was not found in the ActionBar, try to find it in the      */
    /* overflow view, and clicked on it.                                                      */
    /*Log.v("EsspressoShortcuts", """checkMenuItemIsNotDisplayed (id)
        |id:                  ${this}
        |menuItemText:        ${menuItemText}
        |isMenuItemDisplayed: $isMenuItemDisplayed
    """.trimMargin())*/

    if (!isMenuItemDisplayed) {
        val overflowViewId = R.id.title
        try {
            Espresso.openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        } catch (e: Exception) {
            //This is normal. Maybe we don't have overflow menu.
        }

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(overflowViewId),
                withTextStartWithString(menuItemText)
            ))
            .checkIsNotDisplayed()
    }
}

fun withTextStartWithString(itemTextMatcher: String): Matcher<Any> {
    return object : TypeSafeMatcher<Any>() {

        override fun describeTo(description: Description) {
            description.appendText("with text beginning with: $itemTextMatcher")
        }

        override fun matchesSafely(item: Any?): Boolean {
            if (item !is TextView) {
                Log.v("EspressoShortcuts", """matchesSafely false - item: ${item} """)
                return false
            }

            val isTrue = item.text.toString().startsWith(itemTextMatcher)
            Log.v("EspressoShortcuts", """matchesSafely $isTrue - 
                |itemTextMatcher:      ${itemTextMatcher} 
                |item.text.toString(): ${item.text.toString()} 
                |""".trimMargin())
            return item.text.toString().startsWith(itemTextMatcher)
        }
    }
}

//-------------------------------------------------------------------------------

fun showFileDetailsViewForTestFolderName(id:Int, testFolderName: String) {
    Espresso.onView(
        infoImageOnRowWithFileName(
            ViewMatchers.withId(id),
            testFolderName))
        .perform(ViewActions.click())
}

/*
    matchesSafely(...) will return true on a first list view row, containing 'infoImageId' view
    and a 'fileNameId' with text equal to 'fileName'.
 */
fun infoImageOnRowWithFileName(
    parentMatcher: Matcher<View>,
    fileName: String): Matcher<View> {

    return object : TypeSafeMatcher<View>() {

        var rowFound = false
        var foundFirstInfoImageView: View? = null

        override fun describeTo(description: Description) {
            description.appendText("FileName with text $fileName in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            if (rowFound) return false
//            if (view.id != R.id.infoImageId) return false
            val parent = view.parent
            if (parent == null || parent !is View) return false
//            val fileNameView = parent.findViewById<View>(R.id.fileNameId)
//            if (fileNameView == null || fileNameView !is TextView) return false
//            val contentText = fileNameView.text ?: return false

//            if (
//                !rowFound &&
//                parent is ViewGroup &&
//                parentMatcher.matches(parent) &&
//                contentText == fileName) {
//                foundFirstInfoImageView = view
//                rowFound = true
//                return true
//            }
            return false
        }
    }
}

//-------------------------------------------------------------------------------
/**
 * Click on a view with id == 'viewId' whose parent's parent contain a TextView with
 * text == 'textViewText'
 */

fun clickOnViewWithIdWhoseParentsParentTextViewContainsText(textViewText: String, viewId:Int) {
    Espresso.onView(
        findViewWithIdWhoseParentsParentTextViewContainsText(
            textViewText,
            viewId))
        .perform(ViewActions.click())
}

/*
    matchesSafely(...) will return true on a first list view row, containing 'fileNameId' with
    text equal to 'fileName'.
 */
fun findViewWithIdWhoseParentsParentTextViewContainsText(
    locationName: String,
    viewId: Int): Matcher<View> {

    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("viewId: $viewId in parent's parent ")
//            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            if (view.id != viewId) return false
            val directParent = view.parent
            if (directParent == null || directParent !is View) return false
            val parentOfParent: ViewParent? = (directParent as ViewParent).parent

            if (parentOfParent is ViewGroup) {
                var view: View?
                for (pos in 0.. parentOfParent.childCount) {
                    view = parentOfParent.getChildAt(pos)
                    if (view is TextView && view.text.toString() == locationName) {
                        return true
                    }
                }
            }
            return false
        }
    }
}

/*
    Original

    matchesSafely(...) will return true on a first list view row, containing 'fileNameId' with
    text equal to 'fileName'.
 */
/*fun findViewWithIdWhoseParentsParentTextViewContainsText(
    parentMatcher: Matcher<View>,
    fileName: String): Matcher<View> {

    return object : TypeSafeMatcher<View>() {

        var rowFound = false
        var foundFirstInfoImageView: View? = null

        override fun describeTo(description: Description) {
            description.appendText("FileName with text: $fileName in parent ")
            parentMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View): Boolean {
            if (rowFound) return false
            if (view.id != R.id.fileNameId) return false
            val parent = view.parent
            if (parent == null || parent !is View) return false
            val fileNameView = parent.findViewById<View>(R.id.fileNameId)
            if (fileNameView == null || fileNameView !is TextView) return false
            val contentText = fileNameView.text ?: return false
            if (fileName == "Text note 2019-01-27 15:02.pntxt") {
                Log.v("<top>", """matchesSafely -
                |fileName:    ${fileName}
                |contentText: ${contentText}
                |
            """.trimMargin())
            }

            if (
                !rowFound &&
                parent is ViewGroup &&
                parentMatcher.matches(parent) &&
                contentText == fileName) {
                foundFirstInfoImageView = view
                rowFound = true
                return true
            }
            return false
        }
    }
}*/

//-------------------------------------------------------------------------------

/*
    Verify that adapter contains / doesn't contain 'item'
 */
fun testItemAgainstAdapterData(item: String, itemShouldBeInAdapter: Boolean) {
//    if (itemShouldBeInAdapter) {
//        Espresso.onView(ViewMatchers.withId(R.id.folderListView))
//            .check(ViewAssertions.matches(withAdapterData(withFileName(
//                item))))
//    } else {
//        Espresso.onView(ViewMatchers.withId(R.id.folderListView))
//            .check(ViewAssertions.matches(CoreMatchers.not(withAdapterData(withFileName(
//                item)))))
//    }
}

/*
    Returns true if passed FolderItem contains 'fileName' equal to lookupFileName
 */
private fun withFileName(lookupFileName: String): Matcher<Any> {
    return object : TypeSafeMatcher<Any>() {

        override fun describeTo(description: Description) {
            description.appendText("with file name: $lookupFileName")
        }

        override fun matchesSafely(item: Any?): Boolean {
//            if (item !is FolderItem) {
//                return false
//            }

//            return lookupFileName == item.fileName
            return true
        }
    }
}

/*
    Returns true if adapter contains row that matched 'dataMatcher'
 */
private fun withAdapterData(dataMatcher: Matcher<Any>): Matcher<View> {
    return object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("adapter contains row: ")
            dataMatcher.describeTo(description)
        }

        public override fun matchesSafely(view: View) : Boolean {
            if (view !is AdapterView<*>) {
                return false
            }

            val adapter = view.adapter
            (0 until adapter.count).forEach { i ->
                if (dataMatcher.matches(adapter.getItem(i))) {
                    return true
                }
            }

            return false
        }
    }
}
