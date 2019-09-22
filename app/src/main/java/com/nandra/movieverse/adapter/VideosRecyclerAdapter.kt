package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import com.nandra.movieverse.network.VideoData
import kotlinx.android.synthetic.main.item_detail_videos.view.*

class VideosRecyclerAdapter(
    var listVideo: List<VideoData>
) : RecyclerView.Adapter<VideosRecyclerAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_videos, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentVideo = listVideo[position]
        Glide.with(holder.itemView.context)
            .load("https://img.youtube.com/vi/${currentVideo.key}/default.jpg")
            .into(holder.itemView.detail_videos_thumbnail)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}