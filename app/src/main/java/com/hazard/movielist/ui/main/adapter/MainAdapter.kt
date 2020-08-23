package com.hazard.movielist.ui.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hazard.movielist.R
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.GenreItem
import com.hazard.movielist.data.model.Movie

import kotlinx.android.synthetic.main.genre_item.view.*

class MainAdapter(
    private val genres: ArrayList<GenreItem>) : RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    //on user click the item
    var onItemClick: OnItemClick? = null

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(genre : GenreItem) {
            itemView.title.text = genre.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.genre_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = this.genres.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(genres[position])
        holder.itemView.setOnClickListener {
            onItemClick?.onItemClick(genres[position])
        }
    }

    fun addData(genres: List<GenreItem>) {
        this.genres.addAll(genres)
    }

    interface OnItemClick{
        fun onItemClick(genre: GenreItem)
    }
}