package com.nandra.movieverse.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmSearch
import com.endiar.movieverse.core.domain.usecase.RemoteUseCase
import com.endiar.movieverse.core.utils.Constant

class SearchViewModel @ViewModelInject constructor(
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private var currentQuery = ""
    private var currentFilmTypeValue = ""

    private var currentSource: LiveData<Resource<List<FilmSearch>>>? = null
    val searchMediatorLiveData: MediatorLiveData<Resource<List<FilmSearch>>> = MediatorLiveData()

    fun searchMovie(query: String) {
        if (query != currentQuery || currentFilmTypeValue != Constant.MOVIE_FILM_TYPE) {
            removeSource()
            currentSource = remoteUseCase.searchMovie(query).asLiveData()
            currentSource?.run {
                searchMediatorLiveData.addSource(this) {
                    if (it is Resource.Success) {
                        currentQuery = query
                        currentFilmTypeValue = Constant.MOVIE_FILM_TYPE
                    }
                    searchMediatorLiveData.value = it
                }
            }
        }
    }

    fun searchTV(query: String) {
        if (query != currentQuery || currentFilmTypeValue != Constant.TV_FILM_TYPE) {
            removeSource()
            currentSource = remoteUseCase.searchTV(query).asLiveData()
            currentSource?.run {
                searchMediatorLiveData.addSource(this) {
                    if (it is Resource.Success) {
                        currentQuery = query
                        currentFilmTypeValue = Constant.TV_FILM_TYPE
                    }
                    searchMediatorLiveData.value = it
                }
            }
        }
    }

    fun resetSearch() {
        removeSource()
    }

    private fun removeSource() {
        currentSource?.let {
            searchMediatorLiveData.removeSource(it)
        }
    }

}