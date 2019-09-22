package com.nandra.movieverse.network

import com.google.gson.annotations.SerializedName


data class Videos(
    @SerializedName("results")
    val videoData: List<VideoData>
)