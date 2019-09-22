package com.nandra.movieverse.network.response


import com.google.gson.annotations.SerializedName
import com.nandra.movieverse.network.Film

data class DiscoverResponse(
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    val results: List<Film>
)