package au.com.kbrsolutions.melbournepublictransport.favoritestops

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentFavoriteStopsBinding
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopListener.Companion.DELETE_FAVORITE_STOP
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopListener.Companion.DEPARTURES
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopListener.Companion.LIST_VIEW_ROW
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopListener.Companion.SHOW_STOP_FACILITY
import au.com.kbrsolutions.melbournepublictransport.favoritestops.FavoriteStopListener.Companion.SHOW_STOP_ON_MAP
import au.com.kbrsolutions.melbournepublictransport.utilities.obtainViewModel

class FavoriteStopsFragment : Fragment() {

    lateinit var favoriteStopsViewModel: FavoriteStopsViewModel
//    private lateinit var viewDataBinding: FragmentFavoriteStopsBinding

    // fixLater: Sep 27, 2019 - below is needed to be able to run
    //           FavoriteStopsFragmentTest.clickShowDepartures_navigateToDeparturesFragment.
    var navigateToNextDeparturesRequestMillis = 0L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val viewDataBinding: FragmentFavoriteStopsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorite_stops, container, false)

        // https://medium.com/androiddevelopers/viewmodels-a-simple-example-ed5ac416317e
        favoriteStopsViewModel = obtainViewModel(FavoriteStopsViewModel::class.java, null)

        // fixLater: Sep 27, 2019 - remove below after debugging done
        favoriteStopsViewModel.onPrintStopsIds()

        viewDataBinding.favoriteStopsViewModel = favoriteStopsViewModel

        navigateToNextDeparturesRequestMillis = System.currentTimeMillis()
        // Add an Observer on the state variable for Navigating when STOP button is pressed.
        favoriteStopsViewModel.navigateToNextDepartures.observe(this, Observer { favoriteStop ->
            favoriteStop?.let {
                // Also: https://stackoverflow.com/questions/28929637/difference-and-uses-of-oncreate-oncreateview-and-onactivitycreated-in-fra
                this.findNavController().navigate(
                    FavoriteStopsFragmentDirections
                        .actionFavoriteStopsFragmentToNextDeparturesFragment(
                            favoriteStop.stopId,
                            favoriteStop.locationName,
                            navigateToNextDeparturesRequestMillis
                        ))
                // Reset state to make sure we only navigate once, even if the device
                // has a configuration change.
                favoriteStopsViewModel.doneNavigating()
            }
        })

        val adapter = FavoriteStopsAdapter(FavoriteStopListener { favoriteStop, clickedViewName ->
            when (clickedViewName) {
                LIST_VIEW_ROW -> favoriteStopsViewModel.onListViewClick(favoriteStop.id)
                DELETE_FAVORITE_STOP -> favoriteStopsViewModel.onDeleteFavoriteStop(favoriteStop.stopId)
                SHOW_STOP_ON_MAP -> favoriteStopsViewModel.onShowStopOnMapClicked(favoriteStop.stopId)
                DEPARTURES -> favoriteStopsViewModel.onShowDeparturesClicked(favoriteStop)
                SHOW_STOP_FACILITY -> favoriteStopsViewModel.onShowStopFacilityClicked(favoriteStop.stopId)
            }
        })

        viewDataBinding.favoriteStopsList.adapter = adapter

        favoriteStopsViewModel.favoriteStops.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        /*Specify the current activity as the lifecycle owner of the binding. This is used so that
        the binding can observe LiveData updates*/
        viewDataBinding.lifecycleOwner = this

        setHasOptionsMenu(true)
        return viewDataBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorite_stops, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /*
        Below will handle menu items that do not require navigation to some other fragments.
    */
    // fixLater: Oct 12, 2019 - remove R.id.favStops_removeAllFavoriteStops after all deleted
    // fixLater: Oct 12, 2019 - add R.id.favStops_removeAllFavoriteStops when the first added
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.favStops_removeAllFavoriteStops -> removeAllFavoriteStops()
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

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val adapter = FavoriteStopsAdapter(FavoriteStopListener { favoriteStop, clickedViewName ->
            when (clickedViewName) {
                LIST_VIEW_ROW -> favoriteStopsViewModel.onListViewClick(favoriteStop.id)
                DELETE_FAVORITE_STOP -> favoriteStopsViewModel.onDeleteFavoriteStop(favoriteStop.stopId)
                SHOW_STOP_ON_MAP -> favoriteStopsViewModel.onShowStopOnMapClicked(favoriteStop.stopId)
                DEPARTURES -> favoriteStopsViewModel.onShowDeparturesClicked(favoriteStop)
                SHOW_STOP_FACILITY -> favoriteStopsViewModel.onShowStopFacilityClicked(favoriteStop.stopId)
            }
        })

        viewDataBinding.favoriteStopsList.adapter = adapter
//        favoriteStopsViewModel?.loadFavoriteStops()

        favoriteStopsViewModel.favoriteStops.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
    }*/

    private fun removeAllFavoriteStops(): Boolean {
        Log.v("FavoriteStopsFragment", """removeAllFavoriteStops - removeAllFavoriteStops start """)
        favoriteStopsViewModel.onClear()
        return true
    }
}