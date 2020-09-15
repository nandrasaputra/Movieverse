package com.nandra.movieverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nandra.movieverse.R
import kotlinx.android.synthetic.main.item_detail_images.view.*

class ImagesRecyclerViewAdapter : RecyclerView.Adapter<ImagesRecyclerViewAdapter.MyViewHolder>() {

    private var imagePathList: List<String> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_images, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imagePathList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentImagePath = imagePathList[position]
        val url = "https://image.tmdb.org/t/p/w342/"
        Glide.with(holder.itemView.context)
            .load(url + currentImagePath)
            .into(holder.itemView.detail_image_image)
    }

    fun submitList(newList: List<String>) {
        imagePathList = newList
        notifyDataSetChanged()
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}