package au.com.kbrsolutions.melbournepublictransport.utilities

import androidx.test.espresso.IdlingResource

/**
 * Contains a static reference to [IdlingResource], only available in the 'mock' build type.
 */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun increment(source: String) {
        countingIdlingResource.increment()
        println(GLOBAL_PREFIX + " EspressoIdlingResource increment source: $source")
    }

    fun decrement(source: String) {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
        println(GLOBAL_PREFIX + "EspressoIdlingResource decrement - source: $source" + """ - countingIdlingResource.isIdleNow: ${countingIdlingResource.isIdleNow} """)
    }
}