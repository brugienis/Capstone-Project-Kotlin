package au.com.kbrsolutions.melbournepublictransport.data.helper

import android.content.Context
import au.com.kbrsolutions.melbournepublictransport.DebugUtilities
import au.com.kbrsolutions.melbournepublictransport.data.DatabaseDeparture
import au.com.kbrsolutions.melbournepublictransport.data.setId
import au.com.kbrsolutions.melbournepublictransport.departures.DeparturesJsonProcessor
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P

object TestDataGenerator {

    /*
        It looks like when entity definition contains:

            @Entity(tableName = "departure_table")
            data class DatabaseDeparture constructor(
            @PrimaryKey(autoGenerate = true)
            @ColumnInfo(name = "id")
            val id: Int,
            .   .   .

        the starting value of 'id' will be 1.
     */
    private var idValue = 1
    fun generateDataDeparturesList1Row(): List<DatabaseDeparture> {
        idValue = 1
        val databaseDeparture0 =
            DatabaseDeparture(
                idValue,
                1035,
                6,
                15941,
                "2019-08-29T14:02:00Z",
                "30-8",
                "00:02",
                "30 Aug (Fri)",
                false,
                null,
                "182209, 181613, 175928",
                0,
                "City (Flinders Street)",
                "Bonbeach",
                null,
                "Frankston",
                "All stops to Bonbeach",
                1,
                false,
                false,
                1571866226162
            )
        return listOf(databaseDeparture0)
    }
    fun generateDataDeparturesList3Rows(): List<DatabaseDeparture> {
        idValue = 1
        val databaseDeparture0 =
            DatabaseDeparture(
                idValue++,
                1035,
                6,
                15941,
                "2019-08-29T14:02:00Z",
                "30-8",
                "00:02",
                "30 Aug (Fri)",
                false,
                null,
                "182209, 181613, 175928",
                0,
                "City (Flinders Street)",
                "Bonbeach",
                null,
                "Frankston",
                "All stops to Bonbeach",
                1,
                false,
                false,
                1571866226162
            )
        val databaseDeparture1 =
            DatabaseDeparture(
                idValue++,
                1036,
                7,
                15942,
                "2019-08-29T14:03:00Z",
                "30-9",
                "00:03",
                "31 Aug (Fri)",
                false,
                null,
                "182210, 181613, 175928",
                0,
                "City (Flinders Street)1",
                "Bonbeach1",
                null,
                "Frankston1",
                "All stops to Bonbeach1",
                1,
                false,
                false,
                1571866226163
            )
        val databaseDeparture2 =
            DatabaseDeparture(
                idValue++,
                1037,
                8,
                15943,
                "2019-08-29T14:04:00Z",
                "30-10",
                "00:04",
                "32 Aug (Fri)",
                false,
                null,
                "182211, 181613, 175928",
                0,
                "City (Flinders Street)2",
                "Bonbeach2",
                null,
                "Frankston2",
                "All stops to Bonbeach2",
                1,
                false,
                false,
                1571866226164
            )
        return listOf(databaseDeparture0, databaseDeparture1, databaseDeparture2)
    }

    fun getDataDeparturesListNRows(rowsCnt: Int, context: Context): List<DatabaseDeparture> {
        val departuresObjectsFromJson =
            DebugUtilities(context).getDeparturesResponse()
        val databaseDepartureDetails: List<DatabaseDeparture> =
            DeparturesJsonProcessor.buildDepartureDetailsList(departuresObjectsFromJson, context)

        var departuresList = mutableListOf<DatabaseDeparture>()
        databaseDepartureDetails.forEachIndexed { index, databaseDeparture ->
            if (index < rowsCnt) {
                departuresList.add(databaseDeparture.setId(index + 1))
//                println(GLOBAL_PREFIX + """TestDataGenerator - getDataDeparturesListNRows - directionName: $index - ${databaseDeparture.directionName} """)
            }
        }
        println(G_P + """TestDataGenerator - getDataDeparturesListNRows - departuresList size: ${departuresList.size} """)
        return departuresList

//        var list = mutableListOf<DatabaseDeparture>()
//        for (id in 1..100) {
//            list.add(id, generateOneDeparture(id))
//        }
//        return list
    }

    private fun generateOneDeparture(rowId: Int) : DatabaseDeparture {
        val databaseDeparture =
            DatabaseDeparture(
                rowId,
                1037,
                8,
                15943,
                "2019-08-29T14:04:00Z",
                "30-10",
                "00:04",
                "32 Aug (Fri)",
                false,
                null,
                "182211, 181613, 175928",
                0,
                "City (Flinders Street)" + rowId,
                "Bonbeach2",
                null,
                "Frankston2",
                "All stops to Bonbeach" + rowId,
                1,
                false,
                false,
                System.currentTimeMillis()
            )
        return databaseDeparture

    }

}