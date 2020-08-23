package com.hazard.movielist.ui.review.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hazard.movielist.data.model.MovieByGenreModel
import com.hazard.movielist.data.model.MovieVideoResult
import com.hazard.movielist.data.model.ReviewResult
import com.hazard.movielist.data.respository.ReviewRepository
import com.hazard.movielist.ui.base.BaseViewModel
import com.mindorks.framework.mvvm.utils.Resource
import com.mindorks.framework.mvvm.utils.Status
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReviewViewModel(val repository: ReviewRepository) : BaseViewModel() {

    private val movieReviews = MutableLiveData<Resource<ReviewResult>>()

    fun fetchReviews(id: Int,page: Int){
        if (movieReviews.value?.status != Status.LOADMORE){
            val disposable = repository.getReviews(id,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ movieList ->
                    movieReviews.postValue(Resource.success(movieList))
                },{
                    movieReviews.postValue(Resource.error("Unknown Error", null))
                    it.message?.let {
                        movieReviews.postValue(Resource.error(it, null))
                    }
                })
            compositeDisposable.addAll(disposable)
        }
    }

    fun loadMore(genreId: Int,page: Int){
        var newPage = page + 1
        fetchReviews(genreId,newPage)
        movieReviews.postValue(Resource.loadMore(null))
    }


    fun getReviewLiveData(): MutableLiveData<Resource<ReviewResult>>{
        return movieReviews
    }
}