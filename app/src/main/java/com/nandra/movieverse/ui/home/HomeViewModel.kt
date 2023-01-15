package com.nandra.movieverse.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.domain.usecase.RemoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteUseCase: RemoteUseCase) : ViewModel() {

    private var currentTrendingSource: LiveData<Resource<List<FilmGist>>> =
        remoteUseCase.getTrendingFilm().asLiveData()
    private var currentNowPlayingSource: LiveData<Resource<List<FilmGist>>> =
        remoteUseCase.getNowPlayingFilm().asLiveData()

    private var retryFunctionList: MutableList<() -> Unit> = mutableListOf()

    val trendingData = MediatorLiveData<Resource<List<FilmGist>>>()
    val nowPlayingData = MediatorLiveData<Resource<List<FilmGist>>>()

    init {
        trendingData.addSource(currentTrendingSource) {
            trendingData.value = it
            if (it is Resource.Error) {
                retryFunctionList.add(::updateTrending)
            }
        }
        nowPlayingData.addSource(currentNowPlayingSource) {
            nowPlayingData.value = it
            if (it is Resource.Error) {
                retryFunctionList.add(::updateNowPlaying)
            }
        }
    }

    private fun updateTrending() {
        if (trendingData.value !is Resource.Loading) {
            trendingData.removeSource(currentTrendingSource)
            currentTrendingSource = remoteUseCase.getTrendingFilm().asLiveData()
            trendingData.addSource(currentTrendingSource) {
                trendingData.value = it
                if (it is Resource.Error) {
                    retryFunctionList.add(::updateTrending)
                }
            }
        }
    }

    private fun updateNowPlaying() {
        if (nowPlayingData.value !is Resource.Loading) {
            nowPlayingData.removeSource(currentNowPlayingSource)
            currentNowPlayingSource = remoteUseCase.getNowPlayingFilm().asLiveData()
            nowPlayingData.addSource(currentNowPlayingSource) {
                nowPlayingData.value = it
                if (it is Resource.Error) {
                    retryFunctionList.add(::updateNowPlaying)
                }
            }
        }
    }

    fun retryAllFailed() {
        val currentList = retryFunctionList.toList()
        retryFunctionList.clear()
        currentList.forEach {
            it.invoke()
        }
    }

}