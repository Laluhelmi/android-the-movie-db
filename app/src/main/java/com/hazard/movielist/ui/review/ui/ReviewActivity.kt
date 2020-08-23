package com.hazard.movielist.ui.review.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hazard.ReviewModellist.ui.review.adapter.ReviewRVAdapter
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.data.model.ReviewModel
import com.hazard.movielist.ui.base.BaseActivity
import com.hazard.movielist.ui.base.ViewModelFactory
import com.hazard.movielist.ui.movie.adapter.MovieAdapter
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.ui.review.viewmodel.ReviewViewModel
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : BaseActivity() {

    var movieId : Int = 0
    lateinit var viewModel : ReviewViewModel
    lateinit var adapter   : ReviewRVAdapter
    var currentPage   = 1
    var totalPage     = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        setSupprotActionBarSettting()
        getIntentData()
        setAdapter()
        setUpViewModel()
        getReviewLiveData()
    }

    fun getIntentData(){
        movieId = intent.getIntExtra(Constants.INTENT_MOVIE_ID,0)
        supportActionBar?.title = "Reviews"
    }
    fun setAdapter(){

        val layoutManager        = GridLayoutManager(this,1)
        reviewRV.layoutManager  = layoutManager
        adapter = ReviewRVAdapter(arrayListOf())
        reviewRV.adapter  = adapter
        reviewRV.addItemDecoration(
            DividerItemDecoration(
                reviewRV.context,
                (reviewRV.layoutManager as LinearLayoutManager).orientation
            )
        )
        reviewRV.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = reviewRV.layoutManager as GridLayoutManager
                val lastItemPosition = manager.findLastVisibleItemPosition()
                if (lastItemPosition == adapter.itemCount - 1 && currentPage < totalPage){
                    viewModel.loadMore(movieId,currentPage)
                }
            }
        })
    }

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this, ViewModelFactory(apiSource))
            .get(ReviewViewModel::class.java)
    }

    fun getReviewLiveData(){
        viewModel.fetchReviews(movieId,currentPage)
        viewModel.getReviewLiveData().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let { reviewResult ->
                        loadingReview.visibility = View.GONE
                        val totalPage   = reviewResult.total_pages
                        val currentPage = reviewResult.page
                        //set total page
                        adapter.totalPage      = totalPage
                        adapter.currentPage    = currentPage
                        this.currentPage       = currentPage
                        this.totalPage         = totalPage
                        renderItem(reviewResult.result)
                    }
                }
                Status.ERROR ->{
                    loadingReview.visibility = View.GONE
                    it?.message?.let {
                        showToast(it)
                    }
                }
            }
        })
    }

    fun renderItem(review: List<ReviewModel>){
        adapter.addData(review)
        adapter.notifyDataSetChanged()
        if (review.size == 0){
            messageNodata.visibility = View.VISIBLE
            messageNodata.text       = "No Review"
        }
    }

}