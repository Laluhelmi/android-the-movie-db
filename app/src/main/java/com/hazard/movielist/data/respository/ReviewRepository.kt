package com.hazard.movielist.data.respository

import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.data.model.MovieByGenreModel
import io.reactivex.Observable

class ReviewRepository (val apiHelper: ApiHelper){

//    fun getMovieByGenre(genreId: Int,page: Int) : Observable<MovieByGenreModel> {
//        return apiHelper.getMovieByGenre(genreId,page)
//    }
//
//    fun getMovie(id: Int): Observable<Movie> {
//        return apiHelper.gettMovie(id)
//    }
//
//    fun getMovieVideo(id: Int) = apiHelper.getMovieVideo(id )
    fun getReviews(id:Int,page: Int) = apiHelper.getReviews(id,page)

}