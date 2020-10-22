package com.nandra.movieverse.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.endiar.movieverse.core.utils.Constant
import com.google.android.youtube.player.YouTubeIntents
import com.nandra.movieverse.R
import com.nandra.movieverse.ui.YoutubePlayerActivity
import kotlinx.android.synthetic.main.item_detail_videos.view.*


class VideosRecyclerAdapter : RecyclerView.Adapter<VideosRecyclerAdapter.MyViewHolder>() {

    private var listVideoKey: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_detail_videos,
            parent,
            false
        )
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
            val context = holder.itemView.context
            if (isAbleToRunYoutubeAPI(context)) {
                val intent = Intent(context, YoutubePlayerActivity::class.java).apply {
                    putExtra(Constant.EXTRA_YOUTUBE_KEY, currentKey)
                }
                context.startActivity(intent)
            } else {
                val baseUrl = "https://www.youtube.com/watch?v="
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl+currentKey))
                if (browserIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(browserIntent)
                } else {
                    Toast.makeText(context, "Sorry, Your Phone Is Not Supported To Open This Video", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun submitList(newList: List<String>) {
        listVideoKey = newList
        notifyDataSetChanged()
    }

    private fun isAbleToRunYoutubeAPI(context: Context): Boolean {
        return (YouTubeIntents.isYouTubeInstalled((context)) && YouTubeIntents.getInstalledYouTubeVersionCode(
            context
        ) >= 4216)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}