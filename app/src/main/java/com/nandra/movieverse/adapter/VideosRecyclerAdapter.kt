package com.nandra.movieverse.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.utils.Constant
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.YoutubePlayerActivity
import kotlinx.android.synthetic.main.item_detail_videos.view.*

class VideosRecyclerAdapter : RecyclerView.Adapter<VideosRecyclerAdapter.MyViewHolder>() {

    private var listVideoKey: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_videos, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listVideoKey.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentKey = listVideoKey[position]
        Glide.with(holder.itemView.context)
            .load("https://img.youtube.com/vi/${currentKey}/hqdefault.jpg")
            .fallback(R.drawable.img_back_landscape_default)
            .into(holder.itemView.detail_videos_thumbnail)
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, YoutubePlayerActivity::class.java).apply {
                putExtra(Constant.EXTRA_YOUTUBE_KEY, currentKey)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    fun submitList(newList: List<String>) {
        listVideoKey = newList
        notifyDataSetChanged()
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}