package com.nandra.movieverse.network

import com.google.gson.annotations.SerializedName

data class Backdrops(
    @SerializedName("file_path")
    val filepath: String
)