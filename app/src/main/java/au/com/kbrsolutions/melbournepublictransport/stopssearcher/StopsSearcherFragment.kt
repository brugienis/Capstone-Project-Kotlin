package au.com.kbrsolutions.melbournepublictransport.stopssearcher

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import au.com.kbrsolutions.melbournepublictransport.R
import au.com.kbrsolutions.melbournepublictransport.databinding.FragmentStopsSearcherBinding
import au.com.kbrsolutions.melbournepublictransport.utilities.GLOBAL_PREFIX
import au.com.kbrsolutions.melbournepublictransport.utilities.Miscellaneous
import au.com.kbrsolutions.melbournepublictransport.utilities.hideKeyboard
import au.com.kbrsolutions.melbournepublictransport.utilities.obtainViewModel

class StopsSearcherFragment : Fragment() {

    private lateinit var stopsSearcherViewModel: StopsSearcherViewModel
    private lateinit var stopsSearchText: EditText
    private var searchTextChangedByUser = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentStopsSearcherBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_stops_searcher, container, false)

        val stopsSearchTextLayout = binding.stopsSearchTextLayout

        stopsSearchText = binding.stopsSearchText
        stopsSearchText.requestFocus()

        stopsSearchText.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            handleSearchButtonClicked(actionId)
        })

//        val application = requireNotNull(this.activity).application

        stopsSearcherViewModel =obtainViewModel(StopsSearcherViewModel::class.java, null)

        binding.stopsSearcherViewModel = stopsSearcherViewModel

        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

//        stopsSearcherViewModel.insertFavoriteStop()

        return binding.root
    }

    /**
     * Called when user pressed 'search' button. Starts search if search text is not empty.
     */
    @SuppressLint("LongLogTag")
    private fun handleSearchButtonClicked(actionId: Int): Boolean {
        Log.v(GLOBAL_PREFIX + "StopsSearcherFragment", """handleSearchButtonClicked - actionId: ${actionId} """)
        var handled = false
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            val searchTest = getSearchText()
            if (searchTest != null) {
                validateSearchTextAndStartSearch(searchTest)
                handled = true
                hideKeyboard()
            } else {
                setStopsSearchText("")
            }
        }
        return handled
    }

    /**
     * After app published in alpha release, there was a crash in the method below because
     * mListener was null - LG G6, Android 7.0.
     *
     * Moto G4 Play, Android 6.0
     * 11-24 13:20:36.548: E/MonitoringInstr(14820): java.lang.IllegalStateException: Fragment StopsSearcherFragment{483a5af} not attached to Activity
     *
     * Maybe the test executed the Back press. That could force the onDetach(...) call and that
     * would assign null to mListener.
     *
     * Solution - do not do anything in this method if mListener is null - the fragment is
     * already deteched.
     *
     * @param searchText    search text
     */
    private fun validateSearchTextAndStartSearch(searchText: String) {
        var locSearchText: String = searchText
//        if (mListener == null) {
//            return
//        }
        if (Miscellaneous.validateSearchText(locSearchText)) {
            locSearchText = Miscellaneous.capitalizeWords(locSearchText)
            stopsSearcherViewModel.startLineAndStopsSearch(locSearchText)
            showInfoText(getString(R.string.searching_in_progress))
        } else {
            showInfoText(getString(R.string.invalid_search_text))
        }
        // fixLater: Nov 30, 2019 - look at the below later
//        setVisibleView(mSearchInfoTextTV, "validateSearchTextAndStartSearch")
    }

    private fun showInfoText(text: String) {
        stopsSearchText.setText(text)
    }

    /**
     * Use that method to get text from mStopsSearchTextET view.
     *
     * @param searchText
     */
    private fun getSearchText(): String? {
        val searchText: String = stopsSearchText.text.toString().trim { it <= ' ' }
        return if (searchText.isEmpty()) {
            null
        } else searchText
        return searchText
    }

    /**
     * Use that method to set a new text in stopsSearchText view.
     *
     * The OnEditorActionListener.onTextChanged will ignore those changes and will only process
     * changes done by a user changing text.
     *
     * @param searchText
     */
    private fun setStopsSearchText(searchText: String?) {
        searchTextChangedByUser = false
        if (searchText != null) {
            stopsSearchText.setText(searchText)
            stopsSearchText.setSelection(searchText.length)
        }
    }

}
