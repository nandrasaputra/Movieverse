package com.nandra.movieverse.util

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    CANNOT_CONNECT
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        val FAILED = NetworkState(Status.FAILED)
        val CANNOT_CONNECT = NetworkState(Status.CANNOT_CONNECT)
    }
}