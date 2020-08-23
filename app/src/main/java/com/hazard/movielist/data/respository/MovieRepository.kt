package com.hazard.movielist.data.respository

import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.data.model.MovieByGenreModel
import io.reactivex.Observable


class MovieRepository (val apiHelper: ApiHelper){

    fun getMovieByGenre(genreId: Int,page: Int) : Observable<MovieByGenreModel> {
        return apiHelper.getMovieByGenre(genreId,page)
    }

    fun getMovie(id: Int): Observable<Movie>{
        return apiHelper.gettMovie(id)
    }

    fun getMovieVideo(id: Int) = apiHelper.getMovieVideo(id )

}