package com.hazard.movielist.data.api

import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.Movie
import io.reactivex.Observable

class ApiHelper (private val service: ApiService){

    fun getMovieByGenre(genreId: Int,page :Int) = service.getMovieByGenre(genreId,page)
    fun getGenre() = service.getGenre()
    fun gettMovie(id: Int) = service.getMovie(id)
    fun getMovieVideo(id: Int) = service.getMovieVideo(id)
    fun getReviews(id: Int,page: Int) = service.getMovieReviews(id,page)
}