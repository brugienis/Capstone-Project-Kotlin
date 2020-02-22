package au.com.kbrsolutions.melbournepublictransport.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P

@Dao
interface LineStopDetailsDao {
    /*
        Get the list of all 'DatabaseLineStopDetails' rows as a 'LiveData'.
     */
    @Query("SELECT * FROM line_stop_details_table")
    fun getStopsSearcherResults(): LiveData<List<DatabaseLineStopDetails>>

    /*
        Get the 'DatabaseLineStopDetails' row for the given 'id'.
     */
    @Query("SELECT * FROM line_stop_details_table  WHERE id = :id")
    fun getLineStopDetails(id: Int): DatabaseLineStopDetails

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM line_stop_details_table")
    fun clear()

    @Transaction
    fun clearTableAndInsertNewRows(databaseLineStopDetails: List<DatabaseLineStopDetails>) {
        clear()
        insert(databaseLineStopDetails)
    }

    /*
        Insert all 'databaseDeparture' rows from the passed list.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(databaseLineStopDetails: List<DatabaseLineStopDetails>)

    /*
        Modify the previously clicked row's 'show_in_magnified_view' column to 'normal' layout,
        if it is in a 'magnified' layout.

        Toggle the layout of the row with the value of 'id' =='idCurr'.
     */
    @Transaction
    fun toggleMagnifiedNormalView(idCurr: Int) {
        val magnifiedLineStopDetails = getMagnifiedLineStopDetails()
        Log.v(G_P + "LineStopDetailsDao", """toggleMagnifiedNormalView - magnifiedLineStopDetails: ${magnifiedLineStopDetails} """)
        var prevRowId: Int? = null
        magnifiedLineStopDetails?.let {
            flipShowMagnifyView(it.id)
            prevRowId = it.id
        }
        if (prevRowId == null || prevRowId != idCurr) {
            flipShowMagnifyView(idCurr)
        }
    }

    @Query("SELECT * FROM line_stop_details_table WHERE show_in_magnified_view = 1")
    fun getMagnifiedLineStopDetails(): DatabaseLineStopDetails?

    /*
        Flip the value of the show_in_magnified_view column.
     */
    @Query("""
        UPDATE line_stop_details_table 
        SET show_in_magnified_view = NOT show_in_magnified_view 
        WHERE id = :id""")
    fun flipShowMagnifyView(id: Int)

    /*
        Use for debugging.
     */
    @Query("SELECT COUNT() FROM line_stop_details_table")
    fun getLineStopDetailsCount(): Int

}