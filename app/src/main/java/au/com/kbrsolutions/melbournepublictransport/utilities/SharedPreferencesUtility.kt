package au.com.kbrsolutions.melbournepublictransport.utilities

import android.content.Context
import android.preference.PreferenceManager
import android.util.TypedValue
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.common.LatLngDetails
import org.json.JSONObject
import java.util.*

//cla

/**
 *
 * This class allows to set and get SharedPreferences settings.
 *
 */
object SharedPreferencesUtility {

    private val TAG = SharedPreferencesUtility::class.java.simpleName

    /**
     * Returns the value of the 'widget' stop name.
     *
     * @param context
     * @return
     */
    fun getWidgetStopName(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var stopName = prefs.getString(context.getString(R.string.pref_key_widget_stop_name), "")
        if (stopName!!.length == 0) {
            val editor = prefs.edit()
            /* Store default stopId and stopLocationOrLineName : 1071, Flinders Street Station           */
            editor.putString(
                context.getString(R.string.pref_key_widget_stop_id),
                context.getString(R.string.pref_default_widget_stop_id)
            )
            stopName = context.getString(R.string.pref_default_widget_stop_name)
            editor.putString(
                context.getString(R.string.pref_key_widget_stop_name),
                stopName
            )

            editor.apply()
        }
        return stopName
    }

    /**
     *
     * Return 'fixed' location.
     *
     * @param context
     * @return
     */
    fun getDefaultFixedLocationName(context: Context): String {
        val latitude: Float
        return context.getString(R.string.pref_key_default_fixed_location_name)
    }

    /**
     *
     * Return 'fixed' location.
     *
     * @param context
     * @return
     */
    fun getDefaultFixedLocationLatitude(context: Context): String {
        val latitude: Float
        val typedValue = TypedValue()
        context.resources.getValue(
            R.dimen.pref_key_default_fixed_location_latitude,
            typedValue,
            true
        )
        latitude = typedValue.float
        return latitude.toString()
    }

    /**
     *
     * Return 'fixed' location.
     *
     * @param context
     * @return
     */
    fun getDefaultFixedLocationLongitude(context: Context): String {
        val longitude: Float
        val typedValue = TypedValue()
        context.resources.getValue(
            R.dimen.pref_key_default_fixed_location_longitude,
            typedValue,
            true
        )
        longitude = typedValue.float
        return longitude.toString()
    }

    /**
     *
     * Return 'fixed' location position. If it is not assigned, return Flinders Station's
     * latitude and longitude.
     *
     * @param context
     * @return
     */
    fun getFixedLocationLatLng(context: Context): LatLngDetails {
        val latLngDetails: LatLngDetails
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val latitude = prefs.getFloat(
            context.getString(R.string.pref_key_fixed_location_latitude),
            java.lang.Float.NaN
        )
        val longitude = prefs.getFloat(
            context.getString(R.string.pref_key_fixed_location_longitude),
            java.lang.Float.NaN
        )
        if (java.lang.Double.isNaN(latitude.toDouble()) || java.lang.Double.isNaN(longitude.toDouble())) {
            latLngDetails = setDefaultFixedLocation(context)
        } else {
            latLngDetails = LatLngDetails(latitude.toDouble(), longitude.toDouble())
        }
        return latLngDetails
    }

    /**
     *
     * Set default 'fixed' location position.
     *
     * @param   context
     * @return  default fixed' location position latLngDetails.
     */
    fun setDefaultFixedLocation(context: Context): LatLngDetails {
        val latLngDetails: LatLngDetails
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val latitude: Float
        val longitude: Float
        val typedValue = TypedValue()
        context.resources.getValue(
            R.dimen.pref_key_default_fixed_location_latitude,
            typedValue,
            true
        )
        latitude = typedValue.float
        context.resources.getValue(
            R.dimen.pref_key_default_fixed_location_longitude,
            typedValue,
            true
        )
        longitude = typedValue.float
        val editor = prefs.edit()
        /* Store default address - Flinders Street, Melbourne.                            */
        val locationName = context.getString(R.string.pref_key_default_fixed_location_name)
        /* Also store the latitude and longitude so that we can use these to get a precise */
        /* result from our stops nearby search.                                            */
        editor.putString(context.getString(R.string.pref_key_fixed_location_name), locationName)
        editor.putFloat(context.getString(R.string.pref_key_fixed_location_latitude), latitude)
        editor.putFloat(context.getString(R.string.pref_key_fixed_location_longitude), longitude)
        editor.apply()
        latLngDetails = LatLngDetails(latitude.toDouble(), longitude.toDouble())
        return latLngDetails
    }

    /**
     * Returns true if 'release'version setting is true.
     *
     * @param context
     * @return
     */
    fun isReleaseVersion(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.release_version)
    }

    /**
     * Returns true if 'departures' data should be sorted by databaseDeparture time. Its default value
     * is true.
     *
     * @param context
     * @return
     */
    fun isSortDeparturesDataByTime(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val value = prefs.getBoolean(
            context.getString(R.string.departures_data_sort_order_by_time),
            true
        )
        return prefs.getBoolean(
            context.getString(R.string.departures_data_sort_order_by_time),
            true
        )
    }

    /**
     * Set true if 'departures' data should be sorted by databaseDeparture time.
     *
     * @param context
     * @param value     true if 'departures' data should be sorted by databaseDeparture time.
     */
    fun setSortDeparturesDataByTime(context: Context, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(context.getString(R.string.departures_data_sort_order_by_time), value)
        editor.apply()
    }

    /**
     * Returns the map containing disruptions sections visibility.
     *
     * @param context
     * @return
     */
    fun getDisruptionSectionsVisibilityMap(context: Context): Map<String, Boolean> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)

        val outputMap = HashMap<String, Boolean>()
        try {
            if (prefs != null) {
                val prefKey = context.getString(R.string.pref_key_disruptions_sections_visibility)
                val jsonString = prefs.getString(prefKey, JSONObject().toString())
                val jsonObject = JSONObject(jsonString)
                val keysItr = jsonObject.keys()
                while (keysItr.hasNext()) {
                    val key = keysItr.next()
                    val value = jsonObject.get(key) as Boolean
                    outputMap[key] = value
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return outputMap
    }

    /**
     * Returns 'disruptions sections visibility map'.
     *
     * Based on https://stackoverflow.com/questions/7944601/how-to-save-hashmap-to-shared-preferences
     *
     * @param context
     * @param value     map containing disruptions sections visibility.
     */
    fun setDisruptionSectionsVisibilityMap(context: Context, value: Map<String, Boolean>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        if (prefs != null) {
            val jsonObject = JSONObject(value)
            val jsonString = jsonObject.toString()
            val editor = prefs.edit()
            editor.putString(
                context.getString(R.string.pref_key_disruptions_sections_visibility),
                jsonString
            )
            editor.apply()
        }
    }

    /**
     * Returns true if local database was loaded with data from PTV.
     *
     * @param context
     * @return
     */
    fun isShowStopSearcherInstructions(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(context.getString(R.string.show_stop_searcher_instructions), true)
    }

    /**
     * Sets the value of the 'database loaded' flag.
     *
     * @param context
     * @param value
     */
    fun setShowStopSearcherInstructions(context: Context, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(context.getString(R.string.show_stop_searcher_instructions), value)
        editor.apply()
    }

    /**
     *
     * Returns value of the 'use device location' setting.
     *
     * @param context
     * @return
     */
    fun isUseDeviceLocation(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(context.getString(R.string.pref_key_use_device_location), true)
    }

    /**
     *
     * Sets value of the 'use device location' setting.
     *
     * @param context
     * @return
     */
    fun setUseDeviceLocation(context: Context, value: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(context.getString(R.string.pref_key_use_device_location), value)
        editor.apply()
    }

    /**
     * Returns the 'widget' stop Id.
     *
     * @param context
     * @return
     */
    fun getWidgetStopId(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(
            context.getString(R.string.pref_key_widget_stop_id),
            context.getString(R.string.pref_default_widget_stop_id)
        )
    }

    /**
     * Returns the value of the AppBar Vertical Offset.
     *
     * @param context
     * @return
     */
    fun getAppBarVerticalOffset(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(
            context.getString(R.string.pref_key_appbar_vertical_offset),
            Integer.parseInt(context.getString(R.string.pref_default_appbar_vertical_offset))
        )
    }

    /**
     * Sets the value of the AppBar Vertical Offset.
     *
     * @param context
     * @param verticalOffset
     */
    fun setAppBarVerticalOffset(context: Context, verticalOffset: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(
            context.getString(R.string.pref_key_appbar_vertical_offset).toString(),
            verticalOffset
        )
        editor.apply()
    }

    /**
     * Returns the values of the 'transport type' - in PTV JSON response it is 'route_type'.
     *
     * If the key for that preference doesn't exist, select all available route types.
     *
     * @param context
     * @return
     */
    fun getRouteTypes(context: Context): Set<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var transportTypes = prefs.getStringSet(
            context.getString(R.string.pref_key_route_types), HashSet()
        )
        if (transportTypes == null || transportTypes.size == 0) {
            //            Log.v(TAG, "getRouteTypes - using pref_default_route_types_array");
            val editor = prefs.edit()
            /* Store default transport (route) types           */
            transportTypes = HashSet()
            val defaultRouteTypes =
                context.resources.getStringArray(R.array.pref_default_route_types_array)
            Collections.addAll(transportTypes, *defaultRouteTypes)
            editor.putStringSet(
                context.getString(R.string.pref_key_route_types),
                transportTypes
            )
            editor.apply()
        }
        return transportTypes
    }

    /**
     * Returns the values of the 'transport type' - in PTV JSON response it is 'route_type'.
     *
     * If the key for that preference doesn't exist, select all available disruptions types.
     *
     * @param context
     * @return
     */
    fun getDisruptionsTypes(context: Context): Set<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        var transportTypes = prefs.getStringSet(
            context.getString(R.string.pref_key_disruptions_types), HashSet()
        )
        if (transportTypes == null || transportTypes.size == 0) {
            //            Log.v(TAG, "getDisruptionsTypes - using pref_default_disruptions_types_array");
            val editor = prefs.edit()
            /* Store default disruptions types           */
            transportTypes = HashSet()
            val defaultRouteTypes =
                context.resources.getStringArray(R.array.pref_default_disruptions_types_array)
            Collections.addAll(transportTypes, *defaultRouteTypes)
            editor.putStringSet(
                context.getString(R.string.pref_key_disruptions_types),
                transportTypes
            )
            editor.apply()
        }
        return transportTypes
    }

    /**
     * Returns the value of the 'max distance' - it is used in the Stops Nearby search.
     *
     * @param context
     * @return
     */
    /*fun getStopsNearbyMaxDistance(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(
            context.getString(R.string.pref_key_stops_nearby_max_distance),
            context.getString(R.string.pref_default_stops_nearby_max_distance)
        )
    }*/

    /**
     *
     * Sets stops init hint count.
     *
     * @param context
     * @return
     */
    /*fun setDeparturesInitHintCnt(context: Context, stopsInitHintCnt: Int) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putInt(
            context.getString(R.string.pref_key_departures_init_hint_cnt),
            stopsInitHintCnt
        )
        editor.apply()
    }*/

}
