package au.com.kbrsolutions.melbournepublictransport.utilities

import android.graphics.Movie
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory


internal interface PopularMoviesRepository {
    fun getMovies(): LiveData<List<Movie>>
}

internal object MoshiFactory {

    private val moshi : Moshi =  Moshi.Builder()
//        .add(GenreAdapter())
        .add(KotlinJsonAdapterFactory())
        .build()

    fun getInstance() = moshi
}