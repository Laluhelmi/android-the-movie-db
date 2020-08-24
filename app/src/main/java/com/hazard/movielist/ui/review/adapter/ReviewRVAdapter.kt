package com.hazard.ReviewModellist.ui.review.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.hazard.movielist.R
import com.hazard.movielist.data.model.ReviewModel
import kotlinx.android.synthetic.main.genre_item.view.*
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewRVAdapter(
    private val ReviewModels: ArrayList<ReviewModel>
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
                    R.layout.review_item, parent,
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
            return if (position == ReviewModels.size - 1) LOADING_TYPE else REGULAR_TYPE
        }
        else {
            return REGULAR_TYPE
        }
    }

    override fun getItemCount(): Int = this.ReviewModels.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == REGULAR_TYPE){
            (holder as DataViewHolder).bind(ReviewModels[position])
            holder.itemView.setOnClickListener {
                onItemClick?.onItemClick(ReviewModels[position])
            }
        }
    }

    fun removeLasData(){
        this.ReviewModels.removeAt(ReviewModels.size - 1)
    }

    fun addData(ReviewModels: List<ReviewModel>) {
        //remove loading item (last item) when data is loaded
        if (currentPage > 0 && this.ReviewModels.size > 0){
            removeLasData()
        }
        this.ReviewModels.addAll(ReviewModels)
        //add empty object to show loading item
        if (currentPage < totalPage) this.ReviewModels.add(ReviewModel())
    }


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(review : ReviewModel) {
            itemView.author.text  = "Author : "+review.author
            itemView.content.text = review.content
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    interface OnItemClick{
        fun onItemClick(ReviewModel: ReviewModel)
    }
}