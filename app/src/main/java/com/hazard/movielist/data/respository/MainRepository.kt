package com.hazard.movielist.data.respository

import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.Movie
import io.reactivex.Observable
import io.reactivex.Single

class MainRepository (val apiHelper: ApiHelper){

    fun getGenres() : Observable<Genre>{
        return apiHelper.getGenre()
    }

}