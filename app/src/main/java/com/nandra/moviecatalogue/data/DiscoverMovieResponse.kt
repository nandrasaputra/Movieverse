package com.nandra.moviecatalogue.data


import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<Film>
)