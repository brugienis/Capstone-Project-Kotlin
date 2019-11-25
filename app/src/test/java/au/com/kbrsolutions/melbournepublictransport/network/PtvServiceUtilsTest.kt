package au.com.kbrsolutions.melbournepublictransport.network

import org.junit.Assert.assertEquals
import org.junit.Test

class PtvServiceUtilsTest {

    @Test
    fun testBuildTTAPIURL() {
        val textToSign = "/departures/route_type/0/stop/1035?max_results=5&include_cancelled=true&expand=all&expand=stop&expand=route&expand=run&expand=disruption"
        val expected =
            "/v3/departures/route_type/0/stop/1035?max_results=5&include_cancelled=true&expand=all&expand=stop&expand=route&expand=run&expand=disruption&devid=3000165&signature=5D07777F1278F8B57DB94796D1192E0D7DB03808"
        try {
            val actual = PtvServiceUtils.buildTTAPIURL(textToSign)
            assertEquals("Should be equal", expected, actual)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
