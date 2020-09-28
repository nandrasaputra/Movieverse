package com.endiar.movieverse.core.domain.model

data class FilmSearch(
    val id: Int,
    val movieTitle: String,
    val tvTitle: String,
    val voteAverage: Double,
    val releaseDate: String,
    val firstAirDate: String,
    val posterImagePath: String,
    val backdropImagePath: String
)