package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.ASSET_BASE_PATH
import au.com.kbrsolutions.melbournepublictransport.domain.LineStopDetails
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

/**
 * This test will uzse Robolectric to get 'context'
 */
@RunWith(AndroidJUnit4::class)
//@Config(sdk = [Build.VERSION_CODES.O_MR1])
class StopsSearcherJsonProcessorTest {

    private val context: Context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun buildStopsSearcherDetailsList_2EachStopsRoutesOutlets() {
        val favoriteStopIdsSet = mutableSetOf<Int>()
        val stopsSearcherObjectsFromJson =
            DebugUtilities(context).getStopsSearcherResponse("stops_searcher_2_stops_routes_outlets.json")
        val linesAndStopsForSearchResult = StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
            stopsSearcherObjectsFromJson,
            favoriteStopIdsSet
        )
        val health = linesAndStopsForSearchResult.health
        println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - health: ${health} """)
        val lineStopDetailsList = linesAndStopsForSearchResult.lineStopDetailsList

        assertThat(lineStopDetailsList).hasSize(4)

        var stopsCnt = 0
        var routsCnt = 0
        var outletsCnt = 0
        lineStopDetailsList.forEach {
            when (it.lineOrStopType) {
                LineStopDetails.LINE_DETAILS -> routsCnt++
                LineStopDetails.STOP_DETAILS -> stopsCnt++
                else ->outletsCnt++
            }
        }

        assertThat(stopsCnt).isEqualTo(2)
        assertThat(routsCnt).isEqualTo(2)
        // 'outlets' are not extracted from the response
        assertThat(outletsCnt).isEqualTo(0)

       /* lineStopDetailsList.forEachIndexed { index, lineStopDetails ->
            if (index < 10) {
                println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - index: $index lineStopDetails: ${lineStopDetails} """)
            }
        }*/
    }

    @Test
    fun buildStopsSearcherDetailsList_noStopsRoutesOutlets() {
        val favoriteStopIdsSet = mutableSetOf<Int>()
        val stopsSearcherObjectsFromJson =
            DebugUtilities(context).getStopsSearcherResponse("stops_searcher_no_stops_routes_outlets.json")
        val linesAndStopsForSearchResult = StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
            stopsSearcherObjectsFromJson,
            favoriteStopIdsSet
        )
        val health = linesAndStopsForSearchResult.health
        println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - health: ${health} """)
        val lineStopDetailsList = linesAndStopsForSearchResult.lineStopDetailsList

        assertThat(lineStopDetailsList).hasSize(0)

        var stopsCnt = 0
        var routsCnt = 0
        var outletsCnt = 0
        lineStopDetailsList.forEach {
            when (it.lineOrStopType) {
                LineStopDetails.LINE_DETAILS -> routsCnt++
                LineStopDetails.STOP_DETAILS -> stopsCnt++
                else ->outletsCnt++
            }
        }
        assertThat(stopsCnt).isEqualTo(0)
        assertThat(routsCnt).isEqualTo(0)
        // 'outlets' are not extracted from the response
        assertThat(outletsCnt).isEqualTo(0)

       /* lineStopDetailsList.forEachIndexed { index, lineStopDetails ->
            if (index < 10) {
                println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - index: $index lineStopDetails: ${lineStopDetails} """)
            }
        }*/
    }

    @Test
    fun buildStopsSearcherDetailsList_2StopsOnly() {
        val favoriteStopIdsSet = mutableSetOf<Int>()
        val stopsSearcherObjectsFromJson =
            DebugUtilities(context).getStopsSearcherResponse("stops_searcher_2_stops_only.json")
        val linesAndStopsForSearchResult = StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
            stopsSearcherObjectsFromJson,
            favoriteStopIdsSet
        )
        val health = linesAndStopsForSearchResult.health
        println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - health: ${health} """)
        val lineStopDetailsList = linesAndStopsForSearchResult.lineStopDetailsList

        assertThat(lineStopDetailsList).hasSize(2)

        var stopsCnt = 0
        var routsCnt = 0
        var outletsCnt = 0
        lineStopDetailsList.forEach {
            when (it.lineOrStopType) {
                LineStopDetails.LINE_DETAILS -> routsCnt++
                LineStopDetails.STOP_DETAILS -> stopsCnt++
                else ->outletsCnt++
            }
        }

        assertThat(stopsCnt).isEqualTo(2)
        assertThat(routsCnt).isEqualTo(0)
        // 'outlets' are not extracted from the response
        assertThat(outletsCnt).isEqualTo(0)
    }

    @Test
    fun buildStopsSearcherDetailsList_2RoutsOnly() {
        val favoriteStopIdsSet = mutableSetOf<Int>()
        val stopsSearcherObjectsFromJson =
            DebugUtilities(context).getStopsSearcherResponse("stops_searcher_2_routs_only.json")
        val linesAndStopsForSearchResult = StopsSearcherJsonProcessor.buildStopsSearcherDetailsList(
            stopsSearcherObjectsFromJson,
            favoriteStopIdsSet
        )
        val health = linesAndStopsForSearchResult.health
        println(G_P + """StopsSearcherJsonProcessorTest3 - buildStopsSearcherDetailsList - health: ${health} """)
        val lineStopDetailsList = linesAndStopsForSearchResult.lineStopDetailsList

        assertThat(lineStopDetailsList).hasSize(2)

        var stopsCnt = 0
        var routsCnt = 0
        var outletsCnt = 0
        lineStopDetailsList.forEach {
            when (it.lineOrStopType) {
                LineStopDetails.LINE_DETAILS -> routsCnt++
                LineStopDetails.STOP_DETAILS -> stopsCnt++
                else ->outletsCnt++
            }
        }

        assertThat(stopsCnt).isEqualTo(0)
        assertThat(routsCnt).isEqualTo(2)
        // 'outlets' are not extracted from the response
        assertThat(outletsCnt).isEqualTo(0)
    }

    /*private fun getDeparturesJSON(fileName : String = "popular_movies_list.json"): String {
        val inputStream = getInputStreamForJsonFile(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }*/

    @Throws(IOException::class)
    internal open fun getInputStreamForJsonFile(fileName: String): InputStream {
        return FileInputStream(ASSET_BASE_PATH + fileName)
    }
}