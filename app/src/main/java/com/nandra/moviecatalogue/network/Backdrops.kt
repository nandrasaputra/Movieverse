package com.nandra.moviecatalogue.network

import com.google.gson.annotations.SerializedName

data class Backdrops(
    @SerializedName("file_path")
    val filepath: String
)