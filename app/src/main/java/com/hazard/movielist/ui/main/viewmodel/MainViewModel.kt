package com.hazard.movielist.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.respository.MainRepository
import com.hazard.movielist.ui.base.BaseViewModel
import com.mindorks.framework.mvvm.utils.Resource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel (val repository: MainRepository) : BaseViewModel(){

    private val genreLiveData = MutableLiveData<Resource<Genre>>()

    init {
        fetchMovieGenre()
    }
    fun fetchMovieGenre(){
        genreLiveData.postValue(Resource.loading(null))
        val disposable = repository.getGenres()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                genres ->
                genreLiveData.postValue(Resource.success(genres))
            },{
                genreLiveData.postValue(Resource.error(it.localizedMessage!!, null))
            })
        compositeDisposable.addAll(disposable)
    }

    fun liveData() : LiveData<Resource<Genre>>{
        return genreLiveData
    }
}