package com.hazard.movielist.ui.movie.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.GenreItem
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.ui.base.BaseActivity
import com.hazard.movielist.ui.base.ViewModelFactory
import com.hazard.movielist.ui.main.adapter.MainAdapter
import com.hazard.movielist.ui.main.viewmodel.MainViewModel
import com.hazard.movielist.ui.movie.adapter.MovieAdapter
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.loading
import kotlinx.android.synthetic.main.activity_movie.*

class MovieActivity : BaseActivity(),MovieAdapter.OnItemClick {

    var genreId : Int = 0
    var currentPage   = 1
    var totalPage     = 0

    lateinit var adapter:MovieAdapter


    lateinit var viewModel : MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getIntentId()
        setUpViewModel()
        setAdapter()
        getLiveData()
    }

    fun getIntentId(){
        this.genreId  = intent.getIntExtra(Constants.INTENT_GENRE_ID,0)
        val pageTitle = intent.getStringExtra(Constants.INTENT_MOVIE_NAME)
        supportActionBar?.title = pageTitle
    }

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this, ViewModelFactory(apiSource))
            .get(MovieViewModel::class.java)
    }

    override fun onItemClick(movie: Movie) {
        val intent = Intent(this,MovieDetailActivity::class.java)
        intent.putExtra(Constants.INTENT_MOVIE_ID,movie.id)
        intent.putExtra(Constants.INTENT_MOVIE_NAME,movie.title)
        startActivity(intent)
    }

    fun setAdapter(){
        val layoutManager        = GridLayoutManager(this,2)
        listMovie.layoutManager  = layoutManager
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int) : Int{
                if (totalPage > currentPage){
                    return if (position == adapter.itemCount - 1) 2 else 1
                }
                else {
                    return 1
                }
            }
         }
        adapter = MovieAdapter(arrayListOf())
        adapter.onItemClick = this
        listMovie.adapter  = adapter
        listMovie.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val manager = listMovie.layoutManager as GridLayoutManager
                val lastItemPosition = manager.findLastVisibleItemPosition()
                if (lastItemPosition == adapter.itemCount - 1 && currentPage < totalPage){
                    viewModel.loadMore(genreId,currentPage)
                }
            }
        })
    }

    fun getLiveData(){
        viewModel.fetchMovieByGenre(genreId,currentPage)
        viewModel.getMovies().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    loading.visibility = View.GONE
                    it.data?.let {
                        movieByGenreModel ->
                        val totalPage   = movieByGenreModel.total_pages
                        val currentPage = movieByGenreModel.page
                        //set total page
                        adapter.totalPage      = totalPage
                        adapter.currentPage    = currentPage
                        this.currentPage       = currentPage
                        this.totalPage         = totalPage
                        renderItem(movieByGenreModel.result)
                    }
                }
                Status.ERROR ->{
                    loading.visibility = View.GONE
                    it?.message?.let {
                        showToast(it)
                    }
                }
            }
        })
    }

    fun renderItem(movie: List<Movie>){
        adapter.addData(movie)
        adapter.notifyDataSetChanged()
    }
}