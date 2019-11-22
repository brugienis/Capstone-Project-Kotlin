package au.com.kbrsolutions.melbournepublictransport.network

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object PtvServiceUtils {

    private const val DEVELOPER_ID = 3000165
    private const val PRIVATE_KEY = "27a8ac0b-a7d6-494e-a4b9-33119dbf65da"

    private const val PTV_VERSION_NUMBER = "/v3"
    private const val HMAC_SHA1_ALGORITHM = "HmacSHA1"
    private const val DEV_ID = "devid="
    private const val SIGNATURE = "&signature="
    private const val QUESTION_MARK = "?"
    private const val AMPERSAND = "&"
    private const val ZERO = "0"

    /*
        This function is a direct conversion By Kotlin, of the Java method in the
        'production' version of the app.

        I had to manually change one line:

            int intVal = signatureByte & 0xff;
        to

            val intVal = (signatureByte.toInt() and 0xff)

        Kotlin does not allow to do logical 'and (&)' of integer and byte values. The code
        converts byte to int before doing logical 'and'.
     */
    @Throws(Exception::class)
    fun buildTTAPIURL(uri: String): String {
        val uriWithDeveloperID: String
        uriWithDeveloperID = PTV_VERSION_NUMBER +
                uri +
                (if (uri.contains(QUESTION_MARK)) AMPERSAND else QUESTION_MARK) +
                DEV_ID +
                DEVELOPER_ID
        val keyBytes = PRIVATE_KEY.toByteArray()
        val uriBytes = uriWithDeveloperID.toByteArray()
        val signingKey = SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM)
        val mac = Mac.getInstance(HMAC_SHA1_ALGORITHM)
        mac.init(signingKey)
        val signatureBytes = mac.doFinal(uriBytes)
        val signature = StringBuilder(signatureBytes.size * 2)
        for (signatureByte in signatureBytes) {
            val intVal = (signatureByte.toInt() and 0xff)
            if (intVal < 0x10) {
                signature.append(ZERO)  // that will make sure there are always to hexadecimal
                                        // digits for every byte
            }
            signature.append(Integer.toHexString(intVal))
        }
        return uriWithDeveloperID + SIGNATURE + signature.toString().toUpperCase()
    }
}