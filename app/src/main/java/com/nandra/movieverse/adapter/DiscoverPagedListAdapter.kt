package com.nandra.movieverse.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.utils.NetworkState
import com.nandra.movieverse.R
import com.nandra.movieverse.util.DiscoverPagedListDiffUtilCallback
import com.nandra.movieverse.util.FilmType

class DiscoverPagedListAdapter(
    private val type: FilmType,
    private val retryCallback:() -> Unit
) : PagedListAdapter<FilmGist, RecyclerView.ViewHolder>(DiscoverPagedListDiffUtilCallback(type)) {

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.item_discover_recyclerview -> DiscoverViewHolder.create(parent)
            R.layout.item_network_state -> DiscoverNetworkStateViewHolder.create(parent, retryCallback)
            else ->throw IllegalArgumentException("Unknown View Type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)) {
            R.layout.item_discover_recyclerview -> (holder as DiscoverViewHolder).bindView(getItem(position), type)
            R.layout.item_network_state -> {
                (holder as DiscoverNetworkStateViewHolder).bindToNetworkState(networkState)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_discover_recyclerview
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }
}
