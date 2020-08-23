package com.hazard.movielist.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.GenreItem
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.ui.base.BaseActivity
import com.hazard.movielist.ui.base.ViewModelFactory
import com.hazard.movielist.ui.main.adapter.MainAdapter
import com.hazard.movielist.ui.main.viewmodel.MainViewModel
import com.hazard.movielist.ui.movie.ui.MovieActivity
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(),MainAdapter.OnItemClick {
    lateinit var viewModel : MainViewModel
    lateinit var adapter   : MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdapter()
        setUpViewModel()
        getLiveData()
    }

    fun setAdapter(){
        val layoutManager        = GridLayoutManager(this,2)
        listGenres.layoutManager = layoutManager
        adapter = MainAdapter(arrayListOf())
        adapter.onItemClick = this
        listGenres.adapter  = adapter
    }

    override fun onItemClick(genre: GenreItem) {
        val intent = Intent(this,MovieActivity::class.java)
        intent.putExtra(Constants.INTENT_GENRE_ID, genre.id)
        intent.putExtra(Constants.INTENT_MOVIE_NAME, genre.name)
        startActivity(intent)
    }

    fun getLiveData(){
        viewModel.liveData().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    it.data?.genres.let {
                        genres ->
                        loading.visibility = View.GONE
                        message.text = genres?.size.toString()
                        renderItem(genres!!)
                    }
                }
                Status.ERROR ->{
                    loading.visibility = View.GONE
                    message.text = it.message
                }
            }
        })
    }

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this,ViewModelFactory(apiSource))
            .get(MainViewModel::class.java)
    }

    fun renderItem(genres: List<GenreItem>){
        adapter.addData(genres)
        adapter.notifyDataSetChanged()
    }
}