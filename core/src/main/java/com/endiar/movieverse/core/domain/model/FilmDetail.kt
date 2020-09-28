package com.endiar.movieverse.core.domain.model

data class FilmDetail(
    val id: Int,
    val movieTitle: String,
    val runtime: Int,
    val movieReleaseDate: String,
    val tvTitle: String,
    val totalEpisode: Int,
    val tvAirDate: String,
    val voteAverage: Double,
    val genre: List<String>,
    val overview: String,
    val voteCount: Int,
    val posterImagePath: String,
    val backdropImagePath: String,
    val imagePathList: List<String>,
    val castList: List<FilmCast>,
    val videoKeyList: List<String>
)

data class FilmCast(
    val id: Int,
    val name: String,
    val character: String,
    val profileImagePath: String
)