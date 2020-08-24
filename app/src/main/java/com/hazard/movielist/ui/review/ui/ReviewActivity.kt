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
import com.hazard.movielist.ui.base.*
import com.hazard.movielist.ui.movie.adapter.MovieAdapter
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.ui.review.viewmodel.ReviewViewModel
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.activity_review.*
import kotlinx.android.synthetic.main.review_item.view.*

class ReviewActivity : BaseActivity() {

    var movieId : Int = 0
    lateinit var viewModel : ReviewViewModel
    lateinit var adapter   : EndlessAdapter<ReviewModel>

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

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this, ViewModelFactory(apiSource))
            .get(ReviewViewModel::class.java)
    }

    fun setAdapter(){
        adapter = EndlessAdapter(
            R.layout.review_item,
            PaginationModel(datas = arrayListOf()),
            object : EndlessAdapter.BindViewHolder<ReviewModel>{
                override fun bind(holder: View, data: ReviewModel) {
                    holder.author.text  = data.author
                    holder.content.text = data.content
                }

                override fun onItemClick(data: ReviewModel) {
                }
            }
        )
        reviewRV.adapter = adapter
        reviewRV.setLinierLayout()
        reviewRV.addLoadMore(object :EndlessRV.OnLoadMore{
            override fun onLoadMore(currentPage: Int) {
                viewModel.loadMore(movieId = movieId,page = currentPage)
            }
        })
    }

    fun getReviewLiveData(){
        viewModel.fetchReviews(movieId,page = 1)
        viewModel.getReviewLiveData().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.let { reviewResult ->
                        loadingReview.visibility = View.GONE
                        val totalPage   = reviewResult.total_pages
                        val currentPage = reviewResult.page
                        renderItem(currentPage,totalPage,reviewResult.result)
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

    fun renderItem(page: Int, totalPages: Int,review: List<ReviewModel>){
        adapter.updateAdapterData(page,totalPages,review)
        adapter.notifyDataSetChanged()
        if (review.size == 0){
            messageNodata.visibility = View.VISIBLE
            messageNodata.text       = "No Review"
        }
    }

}