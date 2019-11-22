package au.com.kbrsolutions.melbournepublictransport.network

import org.junit.Assert.assertEquals
import org.junit.Test

class PtvServiceUtilsTest {

    @Test
    fun testBuildTTAPIURL() {
        val baseUrl = "http://timetableapi.ptv.vic.gov.au"
        val textToSign = "/v3/departures/route_type/0/stop/1035"
        val expected =
            "http://timetableapi.ptv.vic.gov.au/v3/departures/route_type/0/stop/1035?devid=3000165&signature=B9A9750E19BB9833AF437FDA16CE47F31AD46705"
        try {
            val actual = PtvServiceUtils.buildTTAPIURL(textToSign)
            assertEquals("Should be equal", expected, actual)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}