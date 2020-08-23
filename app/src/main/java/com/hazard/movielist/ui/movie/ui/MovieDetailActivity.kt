package com.hazard.movielist.ui.movie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.ui.base.BaseActivity
import com.hazard.movielist.ui.base.ViewModelFactory
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.ui.review.ui.ReviewActivity
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import kotlinx.android.synthetic.main.activity_movie_detail.*


class MovieDetailActivity : BaseActivity() {

    lateinit var viewModel: MovieViewModel
    var movieId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        setSupprotActionBarSettting()

        setUpViewModel()
        getIntentData()
        getMovieLiveData()
        getMovieVideLiveData()

    }

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this, ViewModelFactory(apiSource))
            .get(MovieViewModel::class.java)
    }

    fun getIntentData(){
        //get id from intent
        movieId = intent.getIntExtra(Constants.INTENT_MOVIE_ID,0)
        supportActionBar?.title = intent.getStringExtra(Constants.INTENT_MOVIE_NAME)
    }

    fun getMovieLiveData(){
        viewModel.getMovie(movieId)
        //get live data
        viewModel.getMovieDetail().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    loadingBar.visibility = View.GONE
                    it.data?.let {
                        renderView(it)
                    }

                }
                Status.ERROR ->{
                    loadingBar.visibility = View.GONE
                    it?.message?.let {
                        showToast(it)
                    }
                }
            }
        })
    }

    fun getMovieVideLiveData(){
        viewModel.getMovieVideo(movieId)
        viewModel.movieVideoLiveData().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    loadingBar.visibility = View.GONE
                    it.data?.let {
                        videoContainer.visibility = View.VISIBLE
                        renderVideo(it.key)
                    }
                }
                Status.ERROR ->{
                    it?.message?.let {
                        showToast(it)
                    }
                }
            }
        })
    }

    fun renderView(movie: Movie){
        val options = RequestOptions()
        options.fitCenter()
        Glide.with(moviePoster.context)
            .load(Constants.TMB_IMAGE_HOST+"${movie.poster_path}")
            .apply(options)
            .into(moviePoster)
        movietitle.setText(movie.original_title)
        overview.setText(movie.overview)
        releaseDate.setText(movie.release_date)
        popularity.setText(movie.popularity.toString())
        homepage.setText(movie.homepage)
        review.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext,
                ReviewActivity::class.java)
            intent.putExtra(Constants.INTENT_MOVIE_ID,movieId)
            startActivity(intent)
        })
    }

    fun renderVideo(videoKey: String){
        lifecycle.addObserver(youtubePlayer)
        youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                val videoId = videoKey
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
    }
}