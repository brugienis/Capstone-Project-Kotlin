package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import au.com.kbrsolutions.melbournepublictransport.MptApplication
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.data.AppDatabase
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherBinding

class StopsSearcherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStopsSearcherBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stops_searcher, container, false)

        val application = requireNotNull(this.activity).application

//        val sleepDaoTest: FavoriteStopDao =
//            Room.inMemoryDatabaseBuilder(context!!, AppDatabase::class.java)
//                // Allowing main thread queries, just for testing.
////            .allowMainThreadQueries()
//                .build().favoriteStopDao()

//        val dataSource = sleepDaoTest
        val dataSource = AppDatabase.getInstance(application).favoriteStopDao()

        val viewModelFactory = StopsSearcherViewModelFactory(
            (requireContext().applicationContext as MptApplication).favoriteStopsRepository)

        val stopsSearcherViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(StopsSearcherViewModel::class.java)

        binding.stopsSearcherViewModel = stopsSearcherViewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.setLifecycleOwner(this)

        setHasOptionsMenu(true)

//        stopsSearcherViewModel.insertFavoriteStop()

        return binding.root
    }
}
