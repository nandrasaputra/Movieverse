package com.nandra.movieverse.ui.favorite

import androidx.lifecycle.*
import com.endiar.movieverse.core.domain.model.FilmFavoriteMovie
import com.endiar.movieverse.core.domain.model.FilmFavoriteTV
import com.endiar.movieverse.core.domain.usecase.LocalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteSharedViewModel @Inject constructor(
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private var currentFavoriteMovieSource : LiveData<List<FilmFavoriteMovie>> =
        localUseCase.getFavoriteMovieList().asLiveData()
    private var currentFavoriteTVSource : LiveData<List<FilmFavoriteTV>> =
        localUseCase.getFavoriteTVList().asLiveData()

    val favoriteMovieMediatorLiveData = MediatorLiveData<List<FilmFavoriteMovie>>()
    val favoriteTVMediatorLiveData = MediatorLiveData<List<FilmFavoriteTV>>()

    init {
        favoriteMovieMediatorLiveData.addSource(currentFavoriteMovieSource) {
            favoriteMovieMediatorLiveData.value = it
        }

        favoriteTVMediatorLiveData.addSource(currentFavoriteTVSource) {
            favoriteTVMediatorLiveData.value = it
        }
    }

    fun removeFavoriteMovie(id: Int) {
        viewModelScope.launch {
            localUseCase.deleteFavoriteMovie(id)
        }
    }

    fun removeFavoriteTV(id: Int) {
        viewModelScope.launch {
            localUseCase.deleteFavoriteTV(id)
        }
    }
}