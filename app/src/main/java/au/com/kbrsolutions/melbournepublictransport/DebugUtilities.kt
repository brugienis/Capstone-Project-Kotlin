package au.com.kbrsolutions.melbournepublictransport

import android.content.Context
import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import au.com.kbrsolutions.melbournepublictransport.stopssearcher.jsondata.StopsSearcherObjectsFromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import java.io.InputStream

//const val ASSET_BASE_PATH = "../app/src/main/assets/"

class DebugUtilities(val context: Context) {

    private val moshi : Moshi =  Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getDeparturesResponse(jsonString: String): DeparturesObjectsFromJson {
//        val json = getDeparturesJSON("departures_results_sample.json")
//        val json = getDeparturesJSON("departures_results_all_expand_options.json")
        val json = getDeparturesJSON(jsonString)
        val adapter: JsonAdapter<DeparturesObjectsFromJson> =
            moshi.adapter(DeparturesObjectsFromJson::class.java)
        return adapter.fromJson(json)!!
    }

    fun getStopsSearcherResponse(jsonString: String) : StopsSearcherObjectsFromJson {
        val json = getDeparturesJSON(jsonString)
        val adapter: JsonAdapter<StopsSearcherObjectsFromJson> =
            moshi.adapter(StopsSearcherObjectsFromJson::class.java)
        return adapter.fromJson(json)!!
    }

    // -------------------------------------------------------------------------------

    private fun getDeparturesJSON(fileName : String): String {
        val inputStream = getInputStreamForJsonFile(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    @Throws(IOException::class)
    private fun getInputStreamForJsonFile(fileName: String): InputStream {
        /*Log.v(
            G_P + "DebugUtilities", """getInputStreamForJsonFile - 
            |context.assets: ${context.assets} 
            |fileName: $fileName 
            |""".trimMargin())*/
        return context.assets.open(fileName)
    }
}