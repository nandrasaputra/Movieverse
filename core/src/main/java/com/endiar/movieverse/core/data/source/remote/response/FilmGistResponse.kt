package com.endiar.movieverse.core.data.source.remote.response


import com.google.gson.annotations.SerializedName

data class FilmGistResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<Film>
)

data class Film(
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val video: Boolean,
    @SerializedName("poster_path")
    val posterPath: String?,
    val id: Int?,
    val adult: Boolean,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_title")
    val originalTitle: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    val overview: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("original_name")
    val tvOriginalName: String,
    @SerializedName("name")
    val tvName: String?,
    @SerializedName("first_air_date")
    val tvAirDate: String?
)