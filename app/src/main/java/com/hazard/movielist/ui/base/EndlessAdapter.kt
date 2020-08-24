package com.hazard.movielist.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hazard.ReviewModellist.ui.review.adapter.ReviewRVAdapter
import com.hazard.movielist.R

import kotlin.collections.ArrayList

class EndlessAdapter<Data>(
    val layout: Int,
    var page      : PaginationModel<Data>,
    val onBindViewHolder: BindViewHolder<Data>
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object{
        val REGULAR_ITEM  = 0
        val LOADING_ITEM  = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == REGULAR_ITEM){
            return ReviewRVAdapter.LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    layout, parent,
                    false
                )
            )
        }
        else {
            return ReviewRVAdapter.LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                R.layout.loading_item, parent,
                false
            )
        )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == REGULAR_ITEM){
            var data = page.datas[position]
            onBindViewHolder.bind(holder.itemView,data)
            holder.itemView.setOnClickListener {
                onBindViewHolder.onItemClick(data)
            }
        }
    }


    override fun getItemCount(): Int {
        return page.datas.size
    }

    override fun getItemViewType(position: Int): Int {
        if (page.currentPage < page.totalPages){
            return if (position == page.datas.size - 1)
                LOADING_ITEM else REGULAR_ITEM
        }
        else {
            return REGULAR_ITEM
        }
    }

    fun removeLasData(){
        (this.page.datas as ArrayList).removeAt(this.page.datas.size - 1)
    }


    fun updateAdapterData(page: Int = 1, totalPage: Int = 1, datas: List<Data>){
        //remove loading item (last item) when data is loaded
        if (this.page.currentPage > 0 && !this.page.datas.isEmpty()){
            removeLasData()
        }
        //update page refreence
        this.page.currentPage = page
        this.page.totalPages  = totalPage
        //
        (this.page.datas as ArrayList).addAll(datas)
        //add empty object to show loading item
        if (this.page.currentPage < this.page.totalPages){
            val copyLastItem = (this.page.datas as ArrayList).last()
            (this.page.datas as ArrayList).add(copyLastItem)
        }

    }


    interface BindViewHolder<Data>{
        fun bind(holder: View,data : Data)
        fun onItemClick(data: Data)
    }
}