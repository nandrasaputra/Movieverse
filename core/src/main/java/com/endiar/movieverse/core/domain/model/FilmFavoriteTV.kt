package com.endiar.movieverse.core.domain.model

data class FilmFavoriteTV(
    override val id: Int,
    override val title: String,
    override val genre: String,
    override val overview: String,
    override val voteAverage: Double,
    override val posterPath: String
) : FavoriteFilm