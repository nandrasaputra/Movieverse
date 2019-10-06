package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.network.Film
import com.nandra.movieverse.ui.HomeFragmentDirections
import com.nandra.movieverse.util.Constant
import kotlinx.android.synthetic.main.item_now_playing.view.*

class NowPlayingRecyclerViewAdapter(
    private val dataList: List<Film>
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
        if(item.posterPath != null) {
            val baseImageUrl = "https://image.tmdb.org/t/p/w500"
            Glide.with(holder.itemView.context)
                .load(baseImageUrl + item.posterPath)
                .into(holder.itemView.item_now_playing_poster)
        }
        holder.itemView.item_now_playing_title.text = item.title
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragmentHome(Constant.MOVIE_FILM_TYPE).setId(item.id.toString())
            holder.itemView.findNavController().navigate(action)
        }
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}