package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.endiar.movieverse.core.utils.NetworkState
import com.endiar.movieverse.core.utils.Status
import com.nandra.movieverse.R
import kotlinx.android.synthetic.main.item_network_state.view.*

class DiscoverNetworkStateViewHolder(
    view: View,
    private val retryCallback:() -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
        itemView.item_retry.setOnClickListener {
            retryCallback.invoke()
        }
    }

    fun bindToNetworkState(state: NetworkState?) {
        itemView.apply {
            when(state?.status) {
                Status.RUNNING -> {
                    item_retry.visibility = View.GONE
                    item_network_progress_bar.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    item_retry.visibility = View.GONE
                    item_network_progress_bar.visibility = View.GONE
                }
                Status.FAILED -> {
                    item_retry.visibility = View.VISIBLE
                    item_network_progress_bar.visibility = View.GONE
                }
                null -> Unit
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
