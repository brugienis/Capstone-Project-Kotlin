package au.com.kbrsolutions.melbournepublictransport.departures

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.common.ProgressBarHandler
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentDeparturesBinding
import au.com.kbrsolutions.melbournepublictransport.departures.DepartureListener.Companion.DEPARTURE_LAYOUT_ID
import au.com.kbrsolutions.melbournepublictransport.departures.DepartureListener.Companion.DISRUPTIONS_IMAGE_ID
import au.com.kbrsolutions.melbournepublictransport.departures.DepartureListener.Companion.LIST_VIEW_ROW
import au.com.kbrsolutions.melbournepublictransport.departures.DepartureListener.Companion.SHOW_STOP_FACILITY
import au.com.kbrsolutions.melbournepublictransport.domain.Departure
import au.com.kbrsolutions.melbournepublictransport.utilities.EspressoIdlingResource
import au.com.kbrsolutions.melbournepublictransport.utilities.G_P
import au.com.kbrsolutions.melbournepublictransport.utilities.SharedPreferencesUtility
import au.com.kbrsolutions.melbournepublictransport.utilities.obtainViewModel

class DeparturesFragment : Fragment() {

    private lateinit var departureViewModel: DeparturesViewModel
    private lateinit var adapter: DeparturesAdapter
//    private lateinit var adapter: DeparturesAdapterSimple
    private lateinit var progressBarHandler: ProgressBarHandler

    companion object {
        private var favoriteStopsRequestedTimMillis: Long = 0
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentDeparturesBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_departures, container, false)

        val arguments = DeparturesFragmentArgs.fromBundle(arguments!!)

            /*
                When in the build.gradle I use lifecycleVersion = '2.2.0-alpha03', the
                    ViewModelProviders.of
                was showing as 'deprecated'.
                The deprecated warning disappeared when I changed to lifecycleVersion = '2.1.0-beta01'.
                This a version used in Google's 'iosched' project.
             */

        departureViewModel = obtainViewModel(DeparturesViewModel::class.java, arguments)

        progressBarHandler = ProgressBarHandler(this, binding.root)
        progressBarHandler.show()

        /*Specify the current activity as the lifecycle owner of the binding. This is used so that
        the binding can observe LiveData updates*/
        binding.lifecycleOwner = this

        binding.departuresViewModel = departureViewModel

        adapter = DeparturesAdapter(DepartureListener{ departure, clickedViewName ->
            println("DeparturesFragment - onCreateView - departure: ${departure} ")
            when (clickedViewName) {
                LIST_VIEW_ROW -> departureViewModel.onListItemViewClick(departure.id)
                DEPARTURE_LAYOUT_ID -> departureViewModel.onDepartureLayoutIdClicked(departure)
                SHOW_STOP_FACILITY -> departureViewModel.onShowStopFacilityClicked(departure.id)
                DISRUPTIONS_IMAGE_ID -> departureViewModel.onDisruptionImageIdClicked(departure)
            }
        })

        binding.departuresList.adapter = adapter

        // Below will prevent loading departures data if configuration change happens
        if (favoriteStopsRequestedTimMillis != arguments.favoriteStopsRequestedTimMillis) {
            favoriteStopsRequestedTimMillis = arguments.favoriteStopsRequestedTimMillis
            departureViewModel.loadDepartures()
        }

        departureViewModel.loadErrMsg.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                progressBarHandler.hide()
            }
        })

        departureViewModel.departureInPtvSortOrder.observe(viewLifecycleOwner, Observer {
            val currentTimeMillis = System.currentTimeMillis()
            println(G_P + """DeparturesFragment - onCreateView - it.size: ${it.size} sorted by time: ${SharedPreferencesUtility.isSortDeparturesDataByTime(context!!)}""")
            it?.let {
                if (it.isNotEmpty()) {
                    progressBarHandler.hide()
                    val departuresSorted = departureViewModel.sortDepartures(
                        it, SharedPreferencesUtility.isSortDeparturesDataByTime(context!!))
                    updateAdapterData(departuresSorted)
                }
                EspressoIdlingResource.decrement("DeparturesFragment - onCreateView departureViewModel.departureInPtvSortOrder.observe")
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun updateAdapterData(databaseDepartures: List<Departure>) {
        activity!!.runOnUiThread {
            adapter.submitList(databaseDepartures)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_departures, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.directions_menu_refresh_data).isVisible = true
        if (SharedPreferencesUtility.isSortDeparturesDataByTime(context!!)) {
            menu.findItem(R.id.directions_menu_sort_by_time).isVisible = false
            menu.findItem(R.id.directions_menu_sort_by_direction).isVisible = true
        } else {
            menu.findItem(R.id.directions_menu_sort_by_time).isVisible = true
            menu.findItem(R.id.directions_menu_sort_by_direction).isVisible = false
        }
        super.onPrepareOptionsMenu(menu)
    }

    // Below is not private because I use it also in the fragment test.
    fun sortDepartureDataAndUpdateAdapter(sortDirectionsByTime: Boolean = true): Boolean {
        val sortedDepartures= departureViewModel.sortDepartureDetailsByTime(sortDirectionsByTime)
        updateAdapterData(sortedDepartures)
        return true
    }

    /*
       Below will handle menu items that do not require navigation to some other fragments.
    */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.directions_menu_refresh_data -> {
                departureViewModel.loadDepartures()
                return true
            }
            R.id.directions_menu_sort_by_time -> {
                SharedPreferencesUtility.setSortDeparturesDataByTime(context!!, true)
                sortDepartureDataAndUpdateAdapter()
                SharedPreferencesUtility.setSortDeparturesDataByTime(context!!, true)
                return true
            }
            R.id.directions_menu_sort_by_direction -> {
                SharedPreferencesUtility.setSortDeparturesDataByTime(context!!, false)
                sortDepartureDataAndUpdateAdapter(false)
                SharedPreferencesUtility.setSortDeparturesDataByTime(context!!, false)
                return true
            }
            else -> navigateBasedOnMenuItem(item)
        }
    }

    /*
        Below will start fragment that in the navigation.xml has the same id as the
        MenuItem id that user clicked on.
    */
    private fun navigateBasedOnMenuItem(item: MenuItem): Boolean {
        return (NavigationUI
            .onNavDestinationSelected(item, view!!.findNavController())
                || super.onOptionsItemSelected(item))
    }

}
