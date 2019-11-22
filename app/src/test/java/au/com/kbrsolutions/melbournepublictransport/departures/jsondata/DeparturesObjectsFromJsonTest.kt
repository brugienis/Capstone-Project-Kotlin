package au.com.kbrsolutions.melbournepublictransport.departures.jsondata

import au.com.kbrsolutions.melbournepublictransport.Responses.expectedDeparturesClass
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

const val ASSET_BASE_PATH = "../app/src/main/assets/"

class DeparturesObjectsFromJsonTest{
    private val moshi : Moshi =  Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Test
    fun createClassFromJson() {
//        val json = getDeparturesJSON("departures_results_sample.json")
        val json = getDeparturesJSON("departures_results_all_expand_options.json")
        val adapter: JsonAdapter<DeparturesObjectsFromJson> =
            moshi.adapter(DeparturesObjectsFromJson::class.java)
        val actual = adapter.fromJson(json)
        val expected = expectedDeparturesClass
        assertEquals(expected.toString(), actual.toString())
    }

    /*
        Below test fails because adaptor does not generate json 'name=value' for every variable in
        the class that has a 'null' value: e.g., for a null variable 'platformNumber'
            platformNumber=null

        the Json string should generate:
            "platform_number": null,
     */
    @Test
    fun createJsonFromClass() {
        val expectedJson = getDeparturesJSON("departures_results_sample.json")
        val departuresDetailsFromJson = expectedDeparturesClass
        val adapter: JsonAdapter<DeparturesObjectsFromJson> =
            moshi.adapter(DeparturesObjectsFromJson::class.java)
        val actualJson = adapter.toJson(departuresDetailsFromJson)
        assertEquals(expectedJson.toString(), actualJson.toString())
    }

    private fun getDeparturesJSON(fileName : String = "popular_movies_list.json"): String {
        val inputStream = getInputStreamForJsonFile(fileName)
        return inputStream.bufferedReader().use { it.readText() }
    }

    @Throws(IOException::class)
    internal open fun getInputStreamForJsonFile(fileName: String): InputStream {
        return FileInputStream(ASSET_BASE_PATH + fileName)
    }

}