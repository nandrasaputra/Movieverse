package com.nandra.movieverse.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.nandra.movieverse.R
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        search_toolbar.apply {
            navigationIcon = activity?.getDrawable(R.drawable.ic_arrow_back_white_24dp)
            setNavigationOnClickListener {
                val inputMethodManager =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
                inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
                activity?.onBackPressed()
            }
        }

        search_searchview.requestFocus()
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }
}