package com.hazard.movielist.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.respository.MainRepository
import com.hazard.movielist.data.respository.MovieRepository
import com.hazard.movielist.data.respository.ReviewRepository
import com.hazard.movielist.ui.main.viewmodel.MainViewModel
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.ui.review.viewmodel.ReviewViewModel

class ViewModelFactory  (private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(MovieRepository(apiHelper)) as T
        }
        else if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(ReviewRepository(apiHelper)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }


}