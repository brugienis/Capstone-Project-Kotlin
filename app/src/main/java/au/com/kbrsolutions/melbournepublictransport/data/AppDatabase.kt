package au.com.kbrsolutions.melbournepublictransport.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import au.com.kbrsolutions.melbournepublictransport.utilities.DATABASE_NAME
import au.com.kbrsolutions.melbournepublictransport.utilities.USE_REAL_DATABASE

/**
 * The Room database for this app.
 *
 * This class creates the Room database - either 'real' or 'in-memory'. The 'in-memory'
 * should be used only in testing.
 */

@Database(
    entities = [DatabaseFavoriteStop::class, DatabaseDeparture::class,
        DatabaseLineStopDetails::class],
    version = 7,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteStopDao(): FavoriteStopDao
    abstract fun departureDao(): DepartureDao
    abstract fun lineStopDetailsDao(): LineStopDetailsDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            Log.v("AppDatabase", """getInstance - instance: ${instance} """)
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppDatabase {
            Log.v("AppDatabase", """buildDatabase - USE_REAL_DATABASE: ${USE_REAL_DATABASE} """)

            return if (USE_REAL_DATABASE) {
                Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            } else {
                val database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()
                populateInMemoryDatabase(database)
                database
            }
        }

        private fun populateInMemoryDatabase(database: AppDatabase) {
            Log.v("AppDatabase", """populateInMemoryDatabase - populateInMemoryDatabase adding: Embarcadero """)
            database.favoriteStopDao().insert(DatabaseFavoriteStop(
                0,
                0,
//                "Embarcadero",
                1035,
                "Carrum Station",
                "Carrum",
                -38.0748978,
                145.122421
            ))
        }
    }}

