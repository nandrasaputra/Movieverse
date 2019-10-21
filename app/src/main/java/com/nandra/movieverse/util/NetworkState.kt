package com.nandra.movieverse.util

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED,
    SERVER_ERROR
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun serverError(msg: String?) = NetworkState(Status.SERVER_ERROR, msg)
        fun error(msg: String?) = NetworkState(Status.FAILED, msg)
    }
}