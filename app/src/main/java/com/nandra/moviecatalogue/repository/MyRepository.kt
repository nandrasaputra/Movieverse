package com.nandra.moviecatalogue.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.nandra.moviecatalogue.database.FavoriteMovie
import com.nandra.moviecatalogue.database.FavoriteTV
import com.nandra.moviecatalogue.database.MovieCatalogueDatabase
import com.nandra.moviecatalogue.network.ConnectivityInterceptor
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDetailApiService
import com.nandra.moviecatalogue.network.apiservice.TheMovieDBDiscoverApiService
import com.nandra.moviecatalogue.network.apiservice.YandexTranslationApiService
import com.nandra.moviecatalogue.network.response.DetailResponse
import com.nandra.moviecatalogue.network.response.DiscoverResponse
import com.nandra.moviecatalogue.network.response.YandexResponse
import retrofit2.Response

class MyRepository(app: Application) {

    private val interceptor = ConnectivityInterceptor(app)
    private val discoverService =
        TheMovieDBDiscoverApiService(interceptor)
    private val detailService =
        TheMovieDBDetailApiService(interceptor)
    private val yandexService =
        YandexTranslationApiService(interceptor)
    private val database = MovieCatalogueDatabase.getInstance(app)

    suspend fun fetchDiscoverMovieResponse() : Response<DiscoverResponse> {
        return discoverService.getMovie("en-US")
    }

    suspend fun fetchDiscoverTVSeriesResponse() : Response<DiscoverResponse> {
        return discoverService.getTVSeries("en-US")
    }

    suspend fun fetchMovieDetailResponse(id: String, parameter: String = "videos,credits,images") : Response<DetailResponse> {
        return detailService.getMovieDetail(id, parameter)
    }

    suspend fun fetchTVDetailResponse(id: String, parameter: String = "videos,credits,images") : Response<DetailResponse> {
        return detailService.getTVDetail(id, parameter)
    }

    suspend fun translateText(text: List<String>) : Response<YandexResponse> {
        return yandexService.translateText("en-id", text)
    }

    suspend fun saveMovieToFavorite(movie: FavoriteMovie) {
        database.favoriteMovieDao().insertToFavoriteMovie(movie)
    }

    suspend fun saveTVToFavorite(tv: FavoriteTV) {
        database.favoriteTVDao().insertToFavoriteTV(tv)
    }

    fun getFavoriteMovieList() : LiveData<List<FavoriteMovie>> {
        return database.favoriteMovieDao().getFavoriteMovieList()
    }

    fun getFavoriteTVList() : LiveData<List<FavoriteTV>> {
        return database.favoriteTVDao().getFavoriteTVList()
    }

    suspend fun removeFavorieMovie(movie: FavoriteMovie) {
        database.favoriteMovieDao().deleteFavoriteMovie(movie)
    }

    suspend fun removeFavorieTV(tv: FavoriteTV) {
        database.favoriteTVDao().deleteFavoriteTV(tv)
    }
}