package com.nandra.movieverse.ui

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandra.movieverse.R
import com.nandra.movieverse.adapter.SearchRecyclerViewAdapter
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.util.Constant
import com.nandra.movieverse.util.NetworkState
import com.nandra.movieverse.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.*


class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedViewModel: SharedViewModel
    private var type = ""
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModel = activity?.run {
            ViewModelProvider(this)[SharedViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedViewModel.searchResultList.observe(this) {
            handleNewData(it)
        }
        sharedViewModel.searchState.observe(this) {
            handleNetworkState(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        type = SearchFragmentArgs.fromBundle(arguments!!).type
        adjustQueryHintText(type)
        search_toolbar.apply {
            navigationIcon = activity?.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                val inputMethodManager =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
                activity?.onBackPressed()
            }
        }
        showKeyboard()
        search_recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            hasFixedSize()
        }
        search_recycler_view.setOnTouchListener { v, _ -> hideKeyboard(v) }
        search_searchview.setOnQueryTextListener(this)
        adjustSearchLanguage()
    }

    override fun onResume() {
        super.onResume()
        sharedViewModel.detailState.value = Constant.STATE_NOSTATE
        sharedViewModel.isOnDetailFragment.value = false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchJob?.run {
            viewLifecycleOwner.lifecycleScope.launch {
                if(this.isActive) {
                    this.cancel()
                    sharedViewModel.attemptSearch(query?: "", type)
                }
            }
        }
        view?.run { hideKeyboard(this) }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText.isNullOrEmpty()){
            setCoverVisibility(true)
            viewLifecycleOwner.lifecycleScope.launch {
                searchJob?.run {
                    if (this.isActive)
                        searchJob!!.cancel()
                }
            }
        } else {
            setCoverVisibility(false)
            search_veil.visibility = View.VISIBLE
            viewLifecycleOwner.lifecycleScope.launch {
                searchJob?.run {
                    if (searchJob!!.isActive)
                        searchJob!!.cancel()
                }
                searchJob = launch {
                    delay(800L)
                    sharedViewModel.attemptSearch(newText, type)
                }
            }
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        checkLastSearch()
    }

    private fun adjustQueryHintText(type: String) {
        if (type == Constant.MOVIE_FILM_TYPE) {
            search_searchview.queryHint = getString(R.string.movie_search_view_query_hint_en)
        } else {
            search_searchview.queryHint = getString(R.string.tv_search_view_query_hint_en)
        }
    }

    private fun prepareSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
    }

    private fun hideKeyboard(view: View) : Boolean {
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view.windowToken, 0)
        search_searchview.findViewById<EditText>(androidx.appcompat.R.id.search_src_text).clearFocus()
        return false
    }

    private fun showKeyboard() {
        search_searchview.requestFocus()
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    private fun handleNewData(data: List<Film>) {
        if (data.isEmpty() && sharedViewModel.isSearchDataLoaded) {
            Toast.makeText(activity, "No Search Result", Toast.LENGTH_SHORT).show()
        }
        search_recycler_view.swapAdapter(SearchRecyclerViewAdapter(data, type), true)
    }

    private fun handleNetworkState(state: NetworkState) {
        when(state) {
            NetworkState.LOADING -> {
                search_progress_bar.visibility = View.VISIBLE
                search_veil.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                search_progress_bar.visibility = View.GONE
                search_veil.visibility = View.GONE
            }
            NetworkState.FAILED -> {
                search_progress_bar.visibility = View.GONE
                search_veil.visibility = View.VISIBLE
            }
            NetworkState.CANNOT_CONNECT -> {
                search_progress_bar.visibility = View.GONE
                search_veil.visibility = View.VISIBLE
                Toast.makeText(activity, "Cannot Connect To Server", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCoverVisibility(state: Boolean) {
        if (state) {
            search_cover.visibility = View.VISIBLE
            search_cover.setOnClickListener {  }       //Prevent ClickThrough
        }
        else
            search_cover.visibility = View.GONE
    }

    private fun checkLastSearch() {
        val lastSearch = search_searchview.query
        if (lastSearch!!.isEmpty()) {
            setCoverVisibility(true)
        } else  {
            sharedViewModel.searchResultList.value?.run {
                search_recycler_view.swapAdapter(SearchRecyclerViewAdapter(this, type), true)
                search_veil.visibility = View.GONE
            }
        }
    }

    private fun adjustSearchLanguage() {
        search_hint_text?.text = getString(R.string.search_no_item_en)
    }
}