package com.endiar.movieverse.core.domain.model

interface FavoriteFilm {
    val id: Int
    val title: String
    val genre: String
    val overview: String
    val voteAverage: Double
    val posterPath: String
}