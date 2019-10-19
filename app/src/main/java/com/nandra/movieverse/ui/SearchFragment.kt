package com.nandra.movieverse.ui

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.nandra.movieverse.R
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var currentLanguage: String = ""
    private lateinit var languageEnglishValue : String
    private lateinit var preferenceLanguageKey : String
    private lateinit var sharedPreferences: SharedPreferences
    private var type = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prepareSharedPreferences()
        type = SearchFragmentArgs.fromBundle(arguments!!).type
        adjustQueryHintText(currentLanguage, type)
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

    override fun onQueryTextSubmit(query: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun adjustQueryHintText(language: String, type: String) {
        if (type == Constant.MOVIE_FILM_TYPE) {
            if (language == languageEnglishValue) {
                search_searchview.queryHint = getString(R.string.movie_search_view_query_hint_en)
            } else {
                search_searchview.queryHint = getString(R.string.movie_search_view_query_hint_id)
            }
        } else {
            if (language == languageEnglishValue) {
                search_searchview.queryHint = getString(R.string.tv_search_view_query_hint_en)
            } else {
                search_searchview.queryHint = getString(R.string.tv_search_view_query_hint_id)
            }
        }
    }

    private fun prepareSharedPreferences() {
        preferenceLanguageKey = getString(R.string.preferences_language_key)
        languageEnglishValue = getString(R.string.preferences_language_value_english)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity)
        currentLanguage = sharedPreferences.getString(preferenceLanguageKey,
            languageEnglishValue)!!
    }
}