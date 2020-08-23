package com.hazard.movielist.data.api

import android.util.Log
import com.hazard.movielist.data.model.*
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Observable
import io.reactivex.Single

class ApiServiceImpl : ApiService{
    val host   = "https://api.themoviedb.org/3"
    val apiKey = "fdb4b491adc0bf44e812987cf600d9bc"

    override fun getMovie(id: Int): Observable<Movie> {
        return Rx2AndroidNetworking.get("$host/movie/$id?api_key=$apiKey")
            .build()
            .getObjectObservable(Movie::class.java)
    }

    override fun getGenre(): Observable<Genre> {
        return Rx2AndroidNetworking.get("$host/genre/movie/list?api_key=$apiKey")
            .build()
            .getObjectObservable(Genre::class.java)
    }

    override fun getMovieByGenre(genreId: Int, page: Int): Observable<MovieByGenreModel> {
        return Rx2AndroidNetworking.get("$host/discover/movie?api_key=$apiKey&with_genres=$genreId&page=$page")
            .build()
            .getObjectObservable(MovieByGenreModel::class.java)
    }

    override fun getMovieVideo(id: Int): Observable<MovieVideoResult> {
        return Rx2AndroidNetworking.get("$host/movie/$id/videos?api_key=$apiKey")
            .build()
            .getObjectObservable(MovieVideoResult::class.java)
    }

    override fun getMovieReviews(id: Int,page: Int): Observable<ReviewResult> {
        return Rx2AndroidNetworking.get("$host/movie/$id/reviews?api_key=$apiKey&language=en-US&page=$page")
            .build()
            .getObjectObservable(ReviewResult::class.java)
    }
}