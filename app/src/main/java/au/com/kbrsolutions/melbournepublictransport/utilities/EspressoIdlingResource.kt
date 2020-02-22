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
//        println("""$G_P EspressoIdlingResource increment (${Miscellaneous.getClassHashCode(this)} ${countingIdlingResource.getCounter()}) source: $source - countingIdlingResource.isIdleNow: ${countingIdlingResource.isIdleNow} """)
    }

    fun decrement(source: String) {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
//        println("""$G_P + "EspressoIdlingResource decrement (${Miscellaneous.getClassHashCode(this)} ${countingIdlingResource.getCounter()}) source: $source - countingIdlingResource.isIdleNow: ${countingIdlingResource.isIdleNow} """)
    }
}