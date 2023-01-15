package com.nandra.movieverse.ui.detail

import androidx.lifecycle.*
import com.endiar.movieverse.core.data.Resource
import com.endiar.movieverse.core.domain.model.FilmDetail
import com.endiar.movieverse.core.domain.usecase.LocalUseCase
import com.endiar.movieverse.core.domain.usecase.RemoteUseCase
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.util.FilmType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val localUseCase: LocalUseCase,
    private val remoteUseCase: RemoteUseCase
) : ViewModel() {

    private var currentRemoteSource: LiveData<Resource<FilmDetail>>? = null
    private var currentLocalSource: LiveData<Boolean>? = null
    private var currentFavoriteStatus = false
    private var currentId = -1
    private var currentFilmTypeValue = ""
    private var currentFilmData: FilmDetail? = null

    val detailRemoteMediatorLiveData = MediatorLiveData<Resource<FilmDetail>>()
    val detailLocalDataMediatorLiveData = MediatorLiveData<Boolean>()

    fun getDetailData(id: Int, filmType: FilmType) {

        if (id == -1) {
            removeMediatorLiveDataSource()
            detailRemoteMediatorLiveData.value = Resource.Error("Error: Cannot Find Data")
            detailLocalDataMediatorLiveData.value = false
            currentId = id
        } else if (currentId != id || currentFilmTypeValue != filmType.typeValue) {
            removeMediatorLiveDataSource()
            when(filmType) {
                is FilmType.FilmTypeMovie -> {
                    getDetailMovieData(id, filmType.typeValue)
                } else -> {
                    getDetailTVData(id, filmType.typeValue)
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            currentFilmData?.let { filmDetail ->
                when(currentFilmTypeValue) {
                    Constant.MOVIE_FILM_TYPE -> {
                        if (currentFavoriteStatus) {
                            localUseCase.deleteFavoriteMovie(currentId)
                        } else {
                            localUseCase.insertToFavoriteMovie(filmDetail)
                        }
                        updateFavoriteStatus()
                    }

                    Constant.TV_FILM_TYPE -> {
                        if (currentFavoriteStatus) {
                            localUseCase.deleteFavoriteTV(currentId)
                        } else {
                            localUseCase.insertToFavoriteTV(filmDetail)
                        }
                        updateFavoriteStatus()
                    }
                }
            }
        }
    }

    private fun getDetailMovieData(id: Int, filmTypeValue: String) {
        currentRemoteSource = remoteUseCase.getDetailMovie(id).asLiveData()
        currentRemoteSource?.run {
            detailRemoteMediatorLiveData.addSource(this) {
                if (it is Resource.Success) {
                    currentId = id
                    currentFilmTypeValue = filmTypeValue
                    currentFilmData = it.data
                }
                detailRemoteMediatorLiveData.value = it
            }
        }
        currentLocalSource = localUseCase.checkFavoriteMovie(id).asLiveData()
        currentLocalSource?.run {
            detailLocalDataMediatorLiveData.addSource(this) {
                currentFavoriteStatus = it
                detailLocalDataMediatorLiveData.value = it
            }
        }
    }

    private fun getDetailTVData(id: Int, filmTypeValue: String) {
        currentRemoteSource = remoteUseCase.getDetailTV(id).asLiveData()
        currentRemoteSource?.run {
            detailRemoteMediatorLiveData.addSource(this) {
                if (it is Resource.Success) {
                    currentId = id
                    currentFilmTypeValue = filmTypeValue
                    currentFilmData = it.data
                }
                detailRemoteMediatorLiveData.value = it
            }
        }
        currentLocalSource = localUseCase.checkFavoriteTV(id).asLiveData()
        currentLocalSource?.run {
            detailLocalDataMediatorLiveData.addSource(this) {
                currentFavoriteStatus = it
                detailLocalDataMediatorLiveData.value = it
            }
        }
    }

    private fun removeMediatorLiveDataSource() {
        currentRemoteSource?.run {
            detailRemoteMediatorLiveData.removeSource(this)
        }
        currentLocalSource?.run {
            detailLocalDataMediatorLiveData.removeSource(this)
        }
    }

    private fun updateFavoriteStatus() {
        currentLocalSource?.run {
            detailLocalDataMediatorLiveData.removeSource(this)
        }
        currentFilmData?.let {
            when(currentFilmTypeValue) {
                Constant.MOVIE_FILM_TYPE -> {
                    currentLocalSource = localUseCase.checkFavoriteMovie(it.id).asLiveData()
                }
                Constant.TV_FILM_TYPE -> {
                    currentLocalSource = localUseCase.checkFavoriteTV(it.id).asLiveData()
                }
            }
        }
        currentLocalSource?.run {
            detailLocalDataMediatorLiveData.addSource(this) {
                detailLocalDataMediatorLiveData.value = it
                currentFavoriteStatus = it
            }
        }
    }

}