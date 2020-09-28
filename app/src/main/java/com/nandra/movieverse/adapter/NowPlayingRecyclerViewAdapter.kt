package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.domain.model.FilmGist
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.home.HomeFragmentDirections
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingRecyclerViewAdapter(
    private var dataList: List<FilmGist>
) : RecyclerView.Adapter<NowPlayingRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_now_playing, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = dataList[position]
        if(item.posterImagePath.isNotEmpty()) {
            val baseImageUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(holder.itemView.context)
                .load(baseImageUrl + item.posterImagePath)
                .into(holder.itemView.item_now_playing_poster)
        }
        holder.itemView.item_now_playing_title.text = item.movieTitle
        holder.itemView.setOnClickListener {
            // NowPlaying always have movie MediaType
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentInHome(Constant.MOVIE_FILM_TYPE, item.id)
            it.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)

    fun submitList(newData: List<FilmGist>?) {
        newData?.let {
            dataList = it
            notifyDataSetChanged()
        }
    }
}
