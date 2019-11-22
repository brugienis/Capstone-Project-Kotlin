package au.com.kbrsolutions.melbournepublictransport.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FavoriteStopDao {
    @Query("select * from favorite_stop_table")
    fun getFavoriteStops(): LiveData<List<DatabaseFavoriteStop>>

    /*
        Use for debugging.
     */
    @Query("select COUNT() from favorite_stop_table")
    fun getFavoriteStopsCount(): Int

    @Query("select * from favorite_stop_table WHERE stop_id = :stopId")
    fun getFavoriteStop(stopId: String): LiveData<DatabaseFavoriteStop>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteStop: DatabaseFavoriteStop)

    /**
     * Delete row for a given stop id.
     */
    @Query("DELETE FROM favorite_stop_table WHERE stop_id = :stopId")
    fun deleteFavoriteStop(stopId: String)

    @Query("select * from favorite_stop_table WHERE stop_id = :stopId")
    fun getTestFavoriteStop(stopId: String): DatabaseFavoriteStop

    @Query("select * from favorite_stop_table WHERE show_in_magnified_view = 1")
    fun getMagnifiedFavoriteStop(): DatabaseFavoriteStop

    /*
        Get the 'databaseDeparture' row for the given 'id'
     */
    @Query("select * from favorite_stop_table WHERE id = :id")
    fun getFavoriteStop(id: Int): DatabaseFavoriteStop

    /*
        Modify the previously clicked row's 'show_in_magnified_view' column to 'normal' layout,
        if it is in a 'magnified' layout.

        Toggle the layout of the row with the value of 'id' =='idCurr'.
     */
    @Transaction
    fun toggleMagnifiedNormalView(idCurr: Int) {
        val magnifiedFavoriteStop = getMagnifiedFavoriteStop()
        var prevRowId: Int? = null
        magnifiedFavoriteStop?.let {
            flipShowMagnifyView(it.id)
            prevRowId = it.id
        }
        if (prevRowId == null || prevRowId != idCurr) {
            flipShowMagnifyView(idCurr)
        }
    }

    /*
        Flip the value of the show_in_magnified_view column.
     */
    @Query("""
        UPDATE favorite_stop_table 
        SET show_in_magnified_view = NOT show_in_magnified_view 
        WHERE id = :id""")
    fun flipShowMagnifyView(id: Int): Int

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM favorite_stop_table")
    fun clear()

    @Query("select * from favorite_stop_table")
    fun getDebugFavoriteStops(): List<DatabaseFavoriteStop>

}