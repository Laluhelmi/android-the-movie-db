package com.hazard.movielist.ui.movie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hazard.movielist.R
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.util.Constants
import kotlinx.android.synthetic.main.genre_item.view.*


class MovieAdapter(
    private val movies: ArrayList<Movie>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{

        val LOADING_TYPE = 1
        val REGULAR_TYPE = 0
    }

    //on user click the item
    var onItemClick: OnItemClick? = null
    var currentPage = 0
    var totalPage   = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == REGULAR_TYPE){
            return DataViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.genre_item, parent,
                    false
                )
            )
        }
        else {
            return LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.loading_item, parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int{
        if (currentPage < totalPage){
            return if (position == movies.size - 1) LOADING_TYPE else REGULAR_TYPE
        }
        else {
            return REGULAR_TYPE
        }
    }

    override fun getItemCount(): Int = this.movies.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       if (getItemViewType(position) == REGULAR_TYPE){
           (holder as DataViewHolder).bind(movies[position])
           holder.itemView.setOnClickListener {
               onItemClick?.onItemClick(movies[position])
           }
       }
    }

    fun removeLasData(){
        this.movies.removeAt(movies.size - 1)
    }

    fun addData(movies: List<Movie>) {
        //remove loading item (last item) when data is loaded
        if (currentPage > 0 && this.movies.size > 0){
            removeLasData()
        }
        this.movies.addAll(movies)
        //add empty object to show loading item
        if (currentPage < totalPage) this.movies.add(Movie())
    }


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie : Movie) {
            itemView.title.text = movie.title
            itemView.item_image.visibility = View.VISIBLE
            val options = RequestOptions()
            options.fitCenter()
            Glide.with(itemView.item_image.context)

                .load(Constants.TMB_IMAGE_HOST+"${movie.poster_path}")
                .apply(options)
                .into(itemView.item_image)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    interface OnItemClick{
        fun onItemClick(movie: Movie)
    }
}