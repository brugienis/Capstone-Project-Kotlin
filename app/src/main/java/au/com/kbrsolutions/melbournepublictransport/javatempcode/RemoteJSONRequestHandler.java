package au.com.kbrsolutions.melbournepublictransport.javatempcode;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/*
    Used only in the Unit Test, to help help debug version converted to Kotlin.
 */
public class RemoteJSONRequestHandler {

    private final static int DEVELOPER_ID = 3000165;
    private final static String PRIVATE_KEY = "27a8ac0b-a7d6-494e-a4b9-33119dbf65da";

    private static final String GET = "GET";
//    private final static String PTV_BASE_URL = "https://timetableapi.ptv.vic.gov.au";


    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static final String DEV_ID = "devid=";
    private static final String SIGNATURE = "&signature=";
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String ZERO = "0";

    /*
        This method was copied from the 'production' version of the app, to help debug version
        converted to Kotlin.
     */
    public static String buildTTAPIURL(final String baseUrl, final String uri) throws Exception {
        String uriWithDeveloperID = uri + (uri.contains(QUESTION_MARK) ? AMPERSAND : QUESTION_MARK) +
                DEV_ID + DEVELOPER_ID;
        byte[] keyBytes = PRIVATE_KEY.getBytes();
        byte[] uriBytes = uriWithDeveloperID.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        byte[] signatureBytes = mac.doFinal(uriBytes);
        StringBuilder signature = new StringBuilder(signatureBytes.length * 2);
        for (byte signatureByte : signatureBytes) {
            int intVal = signatureByte & 0xff;
            if (intVal < 0x10){
                signature.append(ZERO);
            }
            signature.append(Integer.toHexString(intVal));
        }
        return
                baseUrl +
                uri + (uri.contains(QUESTION_MARK) ? AMPERSAND : QUESTION_MARK) +
                DEV_ID + DEVELOPER_ID + SIGNATURE + signature.toString().toUpperCase();
    }
}
