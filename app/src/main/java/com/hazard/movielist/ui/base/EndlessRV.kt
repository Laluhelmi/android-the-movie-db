package com.hazard.movielist.ui.base

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class EndlessRV : RecyclerView {


    constructor(context: Context) : super(context) {}
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    fun setGridLayout(column : Int){
        //val layoutManager = GridLayoutManager(this,2)
        layoutManager = GridLayoutManager(context,column)
        adapter?.let {
            (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int) : Int{
                    val adp = adapter as EndlessAdapter<*>
                    if (adp.page.totalPages > adp.page.currentPage){
                        return if (position == adp.itemCount - 1) 2 else 1
                    }
                    else {
                        return 1
                    }
                }
            }
        }
    }

    fun setLinierLayout(){
        layoutManager = GridLayoutManager(context,1)
    }


    fun addLoadMore(callback: OnLoadMore){
        addOnScrollListener(object :OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager          = layoutManager as GridLayoutManager
                val lastItemPosition = (layoutManager as
                        GridLayoutManager).findLastVisibleItemPosition()
                val adp = adapter as EndlessAdapter<*>
                if (lastItemPosition == manager.itemCount - 1 && adp.page.currentPage < adp.page.totalPages){
                    callback.onLoadMore(adp.page.currentPage)
                }
            }
        })
    }

    interface OnLoadMore{
        fun onLoadMore(currentPage: Int)
    }

}