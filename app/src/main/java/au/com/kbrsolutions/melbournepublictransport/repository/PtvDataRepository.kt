package au.com.kbrsolutions.melbournepublictransport.repository

import au.com.kbrsolutions.melbournepublictransport.data.DepartureDao

// fixLater: Oct 23, 2019 - remove class - not used anymore
class PtvDataRepository(private val departureDao: DepartureDao) {

//    /*val departuresLoadingInProgress = MutableLiveData<Boolean>()
//
//    val loadErrMsg = MutableLiveData<String>()
//
//    *//**
//     * A Ptv data that can be shown on the screen.
//     *
//     * To actually load the Ptv data for use, observe the results of getDepartures(...).
//     *//*
//    fun getDepartures(favoriteStopsRequestedTimeMillis: Long): LiveData<List<Departure>> =
//        Transformations.map(departureDao.getDepartures(favoriteStopsRequestedTimeMillis)) {
//            it.asDomainModel()
//        }
//
//    *//**
//     * Refresh the Ptv data stored in the offline cache.
//     *
//     * This function uses the IO dispatcher to ensure the database insert operation
//     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
//     * function is now safe to call from any thread including the Main thread.
//     *//*
//    suspend fun refreshPtvData(path: String, context: Context) {
//        withContext(Dispatchers.IO) {
////            _status.value = DeparturesApiStatus.LOADING
//            var departuresObjectsFromJson: DeparturesObjectsFromJson? = null
//            departuresLoadingInProgress.postValue(true)
//            try {
//                if (USE_HARD_CODED_PVT_RESPONSE) {
//                    departuresObjectsFromJson =
//                        DebugUtilities(context).getDeparturesResponse()
////                if (true) throw RuntimeException("BR test")
//                } else {
//                    val getDeparturesDeferred: Deferred<DeparturesObjectsFromJson> =
//                        PtvApi.retrofitService.getPtvResponse(path)
//                    departuresObjectsFromJson = getDeparturesDeferred.await()
//                }
//                departuresLoadingInProgress.postValue(false)
//
//                var databaseDepartureDetails: List<DatabaseDeparture> = DeparturesJsonProcessor
//                    .buildDepartureDetailsList(departuresObjectsFromJson, context)
//                departureDao.clearTableAndInsertNewRows(databaseDepartureDetails)
////                _status.value = DeparturesApiStatus.DONE
//                departuresLoadingInProgress.postValue(false)
//                Log.v("DeparturesViewModel", """getPtvResponse -  departuresLoadingInProgress.value: ${ departuresLoadingInProgress.value} """)
//            } catch (e: Exception) {
////                _status.value = DeparturesApiStatus.ERROR
////                loadErrMsg.value = "test error rmessage"
//                loadErrMsg.postValue( "Error message: $e")
//            }
//        }
//    }*/
}