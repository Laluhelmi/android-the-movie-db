package com.hazard.movielist.data.api

import com.hazard.movielist.data.model.*
import io.reactivex.Observable
import io.reactivex.Single

interface ApiService {

    fun getMovie(id: Int): Observable<Movie>
    fun getGenre(): Observable<Genre>
    fun getMovieByGenre(genreId : Int,page: Int): Observable<MovieByGenreModel>
    fun getMovieVideo(id: Int) : Observable<MovieVideoResult>
    fun getMovieReviews(id: Int,page: Int) :Observable<ReviewResult>

}