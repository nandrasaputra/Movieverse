package com.endiar.movieverse.core.utils

import com.endiar.movieverse.core.data.source.local.entity.FavoriteMovieEntity
import com.endiar.movieverse.core.data.source.local.entity.FavoriteTVEntity
import com.endiar.movieverse.core.data.source.remote.response.FilmDetailResponse
import com.endiar.movieverse.core.data.source.remote.response.FilmGistResponse
import com.endiar.movieverse.core.domain.model.*

fun mapFilmGistResponseToListFilmGist(filmGistResponse: FilmGistResponse): List<FilmGist> {
    val data = filmGistResponse.results
    return data.map {
        FilmGist(
            it.id ?: -1,
            it.mediaType ?: "",
            it.title ?: "",
            it.tvName ?: "",
            it.posterPath ?: "",
            it.backdropPath ?: ""
        )
    }
}

fun mapFilmDetailResponseToFilmDetail(filmDetailResponse: FilmDetailResponse) : FilmDetail {
    val castList = filmDetailResponse.credits.cast.map {
        FilmCast(
            it.id ?: -1,
            it.name ?: "",
            it.character ?: "",
            it.profilePath ?: ""
        )
    }
    val genreList = filmDetailResponse.genres.map { it.name }
    val imagePathList = filmDetailResponse.images.backdrops.map { it.filepath }
    val videoKeyList = filmDetailResponse.videos.videoData.map { it.key }

    return FilmDetail(
        filmDetailResponse.id ?: -1,
        filmDetailResponse.title ?: "",
        filmDetailResponse.runtime ?: 0,
        filmDetailResponse.releaseDate ?: "",
        filmDetailResponse.tvTitle ?: "",
        filmDetailResponse.tvNumberOfEpisode ?: 0,
        filmDetailResponse.tvAirDate ?: "",
        filmDetailResponse.voteAverage ?: 0.0,
        genreList,
        filmDetailResponse.overview ?: "",
        filmDetailResponse.voteCount ?: 0,
        filmDetailResponse.posterPath ?: "",
        filmDetailResponse.backdropPath ?: "",
        imagePathList,
        castList,
        videoKeyList
    )
}

fun mapFilmDetailToFavoriteMovieEntity(film: FilmDetail) : FavoriteMovieEntity {
    return FavoriteMovieEntity(
        film.id,
        film.movieTitle,
        film.posterImagePath,
        film.backdropImagePath,
        film.voteAverage,
        Constant.MOVIE_FILM_TYPE,
        film.genre.toString(),
        film.overview
    )
}

fun mapFilmDetailToFavoriteTVEntity(film: FilmDetail) : FavoriteTVEntity {
    return FavoriteTVEntity(
        film.id,
        film.tvTitle,
        film.posterImagePath,
        film.backdropImagePath,
        film.voteAverage,
        Constant.TV_FILM_TYPE,
        film.genre.toString(),
        film.overview
    )
}

fun mapFavoriteMovieEntityToFilmFavoriteMovie(entityList: List<FavoriteMovieEntity>) : List<FilmFavoriteMovie> {
    return entityList.map {
        FilmFavoriteMovie(
            it.movieId,
            it.title,
            it.genre,
            it.overview,
            it.voteAverage,
            it.posterPath
        )
    }
}

fun mapFavoriteTVEntityToFilmFavoriteTV(entityList: List<FavoriteTVEntity>) : List<FilmFavoriteTV> {
    return entityList.map {
        FilmFavoriteTV(
            it.tvId,
            it.title,
            it.genre,
            it.overview,
            it.voteAverage,
            it.posterPath
        )
    }
}

fun mapFilmGistResponseToListFilmSearch(filmGistResponse: FilmGistResponse) : List<FilmSearch> {
    return filmGistResponse.results.map {
        FilmSearch(
            it.id ?: -1,
            it.title ?: "",
            it.tvName ?: "",
            it.voteAverage ?: 0.0,
            it.releaseDate ?: "",
            it.tvAirDate ?: "",
            it.posterPath ?: "",
            it.backdropPath ?: ""
        )
    }
}