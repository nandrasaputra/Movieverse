package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.nandra.movieverse.R
import com.nandra.movieverse.util.NetworkState

class DiscoverNetworkStateViewHolder(
    view: View,
    private val retryCallback:() -> Unit
) : RecyclerView.ViewHolder(view) {
    private val progressBar: ProgressBar = view.findViewById(R.id.item_network_progress_bar)
    private val retryButton: ImageView = view.findViewById(R.id.item_retry)

    init {
        retryButton.setOnClickListener {
            retryCallback.invoke()
        }
    }

    fun bindToNetworkState(state: NetworkState?) {
        when(state) {
            NetworkState.LOADING -> {
                retryButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            NetworkState.LOADED -> {
                retryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            NetworkState.FAILED -> {
                progressBar.visibility = View.GONE
                retryButton.visibility = View.VISIBLE
            }
            NetworkState.CANNOT_CONNECT -> {
                progressBar.visibility = View.GONE
                retryButton.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit) : DiscoverNetworkStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            return DiscoverNetworkStateViewHolder(view, retryCallback)
        }
    }

}