package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import au.com.kbrsolutions.melbournepublictransport.common.LatLngDetails
import org.junit.Assert
import org.junit.Test

class StopsSearcherUrlBuilderTest {

    @Test
    fun buildURI_searchTextOnly() {
        val searchText = "abc"
        val latLngDetails: LatLngDetails? = null
        val searchMaxDistanceMeters: Float = 40000F
        val routeTypes: Set<String>? = null

        val expectedUrl = "abc?&max_distance=40000.0"
        val actualUrl = StopsSearcherUrlBuilder.buildURI(
            searchText,
            latLngDetails,
            searchMaxDistanceMeters,
            routeTypes)

        Assert.assertEquals("Should be equal", expectedUrl, actualUrl)
    }

    @Test
    fun buildURI_searchTextAndLatLon() {
        val searchText = "abc"
        val latLngDetails: LatLngDetails? = LatLngDetails(1.0, 2.11)
        val searchMaxDistanceMeters: Float = 40000F
        val routeTypes: Set<String>? = null

        val expectedUrl = "abc?latitude=1.0&longitude=2.11&max_distance=40000.0"
        val actualUrl = StopsSearcherUrlBuilder.buildURI(
            searchText,
            latLngDetails,
            searchMaxDistanceMeters,
            routeTypes)

        Assert.assertEquals("Should be equal", expectedUrl, actualUrl)
    }

    @Test
    fun buildURI_allParams() {
        val searchText = "abc"
        val latLngDetails: LatLngDetails? = LatLngDetails(1.0, 2.11)
        val searchMaxDistanceMeters: Float = 40000F
        val routeTypes = mutableSetOf("0", "1", "2", "3")

        val expectedUrl = "abc?route_types=0&route_types=1&route_types=2&route_types=3&latitude=1.0&longitude=2.11&max_distance=40000.0"
        val actualUrl = StopsSearcherUrlBuilder.buildURI(
            searchText,
            latLngDetails,
            searchMaxDistanceMeters,
            routeTypes)

        Assert.assertEquals("Should be equal", expectedUrl, actualUrl)
    }
}