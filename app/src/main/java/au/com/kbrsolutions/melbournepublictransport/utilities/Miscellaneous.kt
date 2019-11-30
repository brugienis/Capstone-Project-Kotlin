package au.com.kbrsolutions.melbournepublictransport.utilities

import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.NonNull
import java.util.*
import java.util.regex.Pattern

object Miscellaneous {
    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    fun isNetworkAvailable(c: Context): Boolean {
        val cm =
            c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting
    }

    val currentDMNoLeadingZeros: String
        get() {
            val calendar = Calendar.getInstance()
            val dd = calendar[Calendar.DAY_OF_MONTH]
            val month = calendar[Calendar.MONTH] + 1
            return "$dd-$month"
        }

    /**
     * Validate the 'search string' - should contain letters, numbers, spaces, commas and a
     * decimal period.
     *
     * @param searchText
     * @return
     */
    fun validateSearchText(searchText: String): Boolean {
        val validationResult: Boolean
        validationResult = searchText.matches("^[a-zA-Z0-9 .,]+$".toRegex())
        return validationResult
    }

    /**
     * Replace block of spaces with one.
     *
     * @param serachText    search text
     * @return              return departureDetailsList
     */
    fun replaceSearchTextMultipleSpacesWithOne(serachText: String): String {
        return serachText
            .replace(" +".toRegex(), "%20")
            .replace("\\[".toRegex(), "%5B")
            .replace("\\]".toRegex(), "%5D")
    }

    /**
     * Capitalize words.
     *
     * @param searchText    search text
     * @return              return departureDetailsList
     */
    fun capitalizeWords(searchText: String?): String { //        baased on https://stackoverflow.com/questions/43467120/java-replace-characters-with-uppercase-around-before-and-after-specific-charac
//        searchText = "word w'ord wo'rd";
        val result = StringBuffer()
        //        Matcher m = Pattern.compile("\\b(\\w)(\\w*)'(\\w(?:'\\w)*)").matcher(searchText);
        val m =
            Pattern.compile("\\b(\\w)(\\w*)(\\w(?:\\w)*)").matcher(searchText)
        while (m.find()) { //            System.out.print("capitalizeWords - 1: " + m.group(1) + "; 2: " + m.group(2) + "; 3: " + m.group(3) + "\n");
            m.appendReplacement(
                result,
                m.group(1).toUpperCase() +  //                            m.group(2) + "'" +
                        m.group(2) + "" +  //                            m.group(3).toUpperCase());
                        m.group(3)
            )
        }
        m.appendTail(result)
        return result.toString()
    }

    /**
     * Concatenate arrays.
     *
     * from Joachim Sauer
     *
     * http://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java
     */
    @SafeVarargs
    fun <T> concatAll(first: Array<T>, vararg rest: Array<T>): Array<T> {
        var totalLength = first.size
        for (array in rest) {
            totalLength += array.size
        }
        val result = Arrays.copyOf(first, totalLength)
        var offset = first.size
        for (array in rest) {
            System.arraycopy(array, 0, result, offset, array.size)
            offset += array.size
        }
        return result
    }

    /**
     * Calculate in Kilometers between two GEO positions.
     *
     * from https://dzone.com/articles/distance-calculation-using-3
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    fun distanceKm(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) *
                Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) *
                Math.cos(deg2rad(lat2)) *
                Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }

    /**
     * This function converts decimal degrees to radians.
     *
     * @param deg
     * @return
     */
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    /**
     * This function converts radians to decimal degrees.
     *
     * @param rad
     * @return
     */
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    /**
     * Returns object hash code - use for testing.
     *
     * @param   o object
     * @return  object's hashCode
     */
    fun getClassHashCode(o: Any): String {
        return String.format("0x%08X", o.hashCode())
    }//WAIT_TIME_BEFORE_ACCEPTING_NEXT_CLICK_MSECS = 1L * 1000;

    /**
     * Returns time between consecutive clicks before the code will act on. It is in milliseconds.
     *
     * @return  minimum time between clicks
     */
    val timeBeforeAcceptingNextClickMillis: Long
        get() =//WAIT_TIME_BEFORE_ACCEPTING_NEXT_CLICK_MSECS = 1L * 1000;
            700L

    @NonNull
    fun getTransportTypeSummary(keys: Set<String?>): String {
        val summaryText = StringBuilder()
        val keysList: MutableList<String> =
            ArrayList(keys.size)
        /* alphabetical order of transport types (rout_types) */
        val keysOrder = arrayOf("2", "4", "1", "0", "3")
        for (c in keysOrder) {
            if (keys.contains(c)) {
                keysList.add(c)
            }
        }
        for (key in keysList) {
            if (summaryText.length > 0) {
                summaryText.append(", ")
            }
            when (key) {
                "0" -> {
                    summaryText.append("Train")
                }
                "1" -> {
                    summaryText.append("Tram")
                }
                "2" -> {
                    summaryText.append("Bus")
                }
                "3" -> {
                    summaryText.append("Vline")
                }
                "4" -> {
                    summaryText.append("Night Bus")
                }
                else -> throw RuntimeException(
                    Miscellaneous::class.java.simpleName +
                            " - incorrect value of transport type: " + key
                )
            }
        }
        //            Log.v(TAG, "getTransportTypeSummary - summaryText: " + summaryText.toString());
        return summaryText.toString()
    }
}
