package au.com.kbrsolutions.melbournepublictransport

import android.content.Context
import android.util.Log
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.io.InputStream

const val ASSET_BASE_PATH = "../app/src/main/assets/"

class DebugUtilities(val context: Context) {

    private val moshi : Moshi =  Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    public fun getDeparturesResponse(): DeparturesObjectsFromJson {
//        val json = getDeparturesJSON("departures_results_sample.json")
        val json = getDeparturesJSON("departures_results_all_expand_options.json")
        val adapter: JsonAdapter<DeparturesObjectsFromJson> =
            moshi.adapter(DeparturesObjectsFromJson::class.java)
        return adapter.fromJson(json)!!
    }

    private fun getDeparturesJSON(fileName : String = "popular_movies_list.json"): String {
        val inputStream = getInputStreamForJsonFile(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    @Throws(IOException::class)
    private fun getInputStreamForJsonFile(fileName: String): InputStream {
        Log.v("DebugUtilities", """getInputStreamForJsonFile - 
            |fileName: $fileName """.trimMargin())
        return context.assets.open(fileName)
    }
}