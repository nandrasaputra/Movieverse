package com.nandra.movieverse.util

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    SERVER_ERROR
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        val FAILED = NetworkState(Status.FAILED)
        val SERVER_ERROR = NetworkState(Status.SERVER_ERROR)
    }
}