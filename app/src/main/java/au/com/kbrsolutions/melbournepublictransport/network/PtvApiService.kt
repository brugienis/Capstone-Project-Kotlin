package au.com.kbrsolutions.melbournepublictransport.network

import au.com.kbrsolutions.melbournepublictransport.departures.jsondata.DeparturesObjectsFromJson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL = "https://timetableapi.ptv.vic.gov.au"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object pointing to the desired URL
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 *
 * Check - https://square.github.io/retrofit/2.x/retrofit/
 */
interface PtvApiService {
    /**
     * Returns a Retrofit callback that delivers a String
     * The @GET annotation indicates that the 'remaining' endpoint will be requested with the GET
     * HTTP method.
     */
    @GET("{remaining}")
    fun getPtvResponse(@Path("remaining", encoded=true) dynamicPartOfRequest: String):
            Deferred<DeparturesObjectsFromJson>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PtvApi {
    val retrofitService : PtvApiService by lazy { retrofit.create(PtvApiService::class.java) }
}