package au.com.kbrsolutions.melbournepublictransport.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DepartureDao {
    /*
        Get the list of all 'databaseDeparture' rows as a 'LiveData'.

        Return only the rows inserted after the 'millis' time.
     */
    @Query("select * from departure_table WHERE row_insert_time > :millis")
    fun getDepartures(millis: Long): LiveData<List<DatabaseDeparture>>

    /*
        Get the 'databaseDeparture' row for the given 'id'.
     */
    @Query("select * from departure_table  WHERE id = :id")
    fun getDeparture(id: Int): DatabaseDeparture

    @Query("select * from departure_table WHERE show_in_magnified_view = 1")
    fun getMagnifiedDeparture(): DatabaseDeparture

    @Transaction
    fun clearTableAndInsertNewRows(databaseDepartures: List<DatabaseDeparture>) {
        deleteAllDepartures()
        insert(databaseDepartures)
    }

    /*
        Insert all 'databaseDeparture' rows from the passed list.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(databaseDepartures: List<DatabaseDeparture>)

    /*
        Modify the previously clicked row's 'show_in_magnified_view' column to 'normal' layout,
        if it is in a 'magnified' layout.

        Toggle the layout of the row with the value of 'id' =='idCurr'.
     */
    @Transaction
    fun toggleMagnifiedNormalView(idCurr: Int) {
        val magnifiedDeparture = getMagnifiedDeparture()
        Log.v("DepartureDao", """toggleMagnifiedNormalView - magnifiedDeparture: ${magnifiedDeparture} """)
        var prevRowId: Int? = null
        magnifiedDeparture?.let {
            flipShowMagnifyView(it.id)
            prevRowId = it.id
        }
        if (prevRowId == null || prevRowId != idCurr) {
            Log.v("DepartureDao", """toggleMagnifiedNormalView - idCurr: ${idCurr} """)
            flipShowMagnifyView(idCurr)
        }

//        var isPrevMagnified = false
//        if (idPrev != null) isPrevMagnified = getDeparture(idPrev).showInMagnifiedView
//        val isCurrMagnified = getDeparture(idCurr).showInMagnifiedView
//        if (isPrevMagnified) {
//            flipShowMagnifyView(idPrev!!)
//        }

//        if (isPrevMagnified && idPrev != idCurr || !isCurrMagnified) {
//            flipShowMagnifyView(idCurr)
//        }
    }

    /*
        Flip the value of the show_in_magnified_view column.
     */
    @Query("""
        UPDATE departure_table 
        SET show_in_magnified_view = NOT show_in_magnified_view 
        WHERE id = :id""")
    fun flipShowMagnifyView(id: Int)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM departure_table")
    fun deleteAllDepartures()
}