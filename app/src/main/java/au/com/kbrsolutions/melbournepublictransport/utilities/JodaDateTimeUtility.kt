package au.com.kbrsolutions.melbournepublictransport.utilities

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat

/*
    I am still using JodaTime library. The Java 8 Date classes can do the same thing but I am
    getting the warning, that it will only work with SDK 28+, but my minimum version is 19.
 */
object JodaDateTimeUtility {

    fun getLocalTimeFromUtcString(utcTimeStr: String): org.joda.time.DateTime {
        val date = DateTime.parse(
            utcTimeStr,
            DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        )
        return getLocalTime(date)
    }
    fun getDateTimeMillis(utcTimeStr: String): Long {
        return getDateTimeMillis(utcTimeStr)
    }

    private fun getLocalTime(utcTime: org.joda.time.DateTime): org.joda.time.DateTime {
        val dateTimeZone = DateTimeZone.getDefault()
        return utcTime.withZone(dateTimeZone)
    }
}