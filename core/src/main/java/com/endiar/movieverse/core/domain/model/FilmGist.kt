package com.endiar.movieverse.core.domain.model

data class FilmGist (
    val id: Int,
    val mediaType: String,
    val movieTitle: String,
    val tvTitle: String,
    val posterImagePath: String,
    val backdropImagePath: String
)