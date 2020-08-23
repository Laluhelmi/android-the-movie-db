package com.hazard.movielist.ui.movie.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.data.model.MovieByGenreModel
import com.hazard.movielist.data.model.MovieVideo
import com.hazard.movielist.data.respository.MovieRepository
import com.hazard.movielist.ui.base.BaseViewModel
import com.mindorks.framework.mvvm.utils.Resource
import com.mindorks.framework.mvvm.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MovieViewModel(val repository: MovieRepository) : BaseViewModel() {
    //
    private val movies = MutableLiveData<Resource<MovieByGenreModel>>()
    //
    private val movieDetail = MutableLiveData<Resource<Movie>>()
    //
    private val movieVideo   = MutableLiveData<Resource<MovieVideo>>()

    fun fetchMovieByGenre(genreId : Int,page: Int){
        if (movies.value?.status != Status.LOADMORE){
            val disposable = repository.getMovieByGenre(genreId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieList ->
                    movies.postValue(Resource.success(movieList))
                },{
                    movies.postValue(Resource.error("Unknown Error", null))
                    it.message?.let {
                        movies.postValue(Resource.error(it, null))
                    }
                })
            compositeDisposable.addAll(disposable)
        }
    }


    fun getMovie(id: Int){
        val disposable = repository.getMovie(id).
        subscribeOn(Schedulers.io()).
        observeOn(AndroidSchedulers.mainThread()).
        subscribe({
            movieDetail.postValue(Resource.success(it))
        },{
            movies.postValue(Resource.error("Unknown Error", null))
            it.message?.let {
                movieDetail.postValue(Resource.error(it, null))
            }
        })
        compositeDisposable.add(disposable)
    }

    fun getMovieVideo(id: Int){
       val disposable =  repository.getMovieVideo(id)
             .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.result.size > 0) movieVideo.postValue(Resource.success(it.result[0]))

            },{
                it.message?.let {
                  movieVideo.postValue(Resource.error(it,null))
                }
            })
        compositeDisposable.add(disposable)
    }

    fun loadMore(genreId: Int,page: Int){
        var newPage = page + 1
        fetchMovieByGenre(genreId,newPage)
        movies.postValue(Resource.loadMore(null))
    }

    //get live data
    fun getMovies() : LiveData<Resource<MovieByGenreModel>> {
        return movies
    }

    //get live data
    fun getMovieDetail() : LiveData<Resource<Movie>>{
        return movieDetail
    }

    //get movie video live data
    fun movieVideoLiveData(): LiveData   <Resource<MovieVideo>>{
        return movieVideo
    }
}