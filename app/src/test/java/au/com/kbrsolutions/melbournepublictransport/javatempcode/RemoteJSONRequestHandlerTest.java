package au.com.kbrsolutions.melbournepublictransport.javatempcode;

import org.junit.Test;

import static org.junit.Assert.*;

public class RemoteJSONRequestHandlerTest {

    @Test
    public void testBuildTTAPIURL() {
        String baseUrl = "http://timetableapi.ptv.vic.gov.au";
        String textToSign = "/v3/departures/route_type/0/stop/1035";
        String expectedResult =   "http://timetableapi.ptv.vic.gov.au/v3/departures/route_type/0/stop/1035?devid=3000165&signature=B9A9750E19BB9833AF437FDA16CE47F31AD46705";
        try {
            String actualResult = RemoteJSONRequestHandler.buildTTAPIURL(baseUrl, textToSign);
            assertEquals("Should be equal", expectedResult, actualResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}