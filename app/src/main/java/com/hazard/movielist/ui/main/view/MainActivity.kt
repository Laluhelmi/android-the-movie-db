package com.hazard.movielist.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hazard.ReviewModellist.ui.review.adapter.ReviewRVAdapter
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.Genre
import com.hazard.movielist.data.model.GenreItem
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.data.model.ReviewModel
import com.hazard.movielist.ui.base.*
import com.hazard.movielist.ui.main.adapter.MainAdapter
import com.hazard.movielist.ui.main.viewmodel.MainViewModel
import com.hazard.movielist.ui.movie.ui.MovieActivity
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.genre_item.view.*
import kotlinx.android.synthetic.main.review_item.view.*

class MainActivity : BaseActivity(),MainAdapter.OnItemClick {
    lateinit var viewModel : MainViewModel
    lateinit var adapter   : EndlessAdapter<GenreItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setAdapter()
        setUpViewModel()
        getLiveData()
    }

    fun setAdapter(){
        var paginationModel = PaginationModel<GenreItem>(datas = arrayListOf())
        val onBind = object: EndlessAdapter.BindViewHolder<GenreItem>{
            override fun bind(holder: View, data: GenreItem) {
                holder.title.text = data.name
            }
            override fun onItemClick(data: GenreItem) {
                val intent = Intent(applicationContext,MovieActivity::class.java)
                intent.putExtra(Constants.INTENT_GENRE_ID, data.id)
                intent.putExtra(Constants.INTENT_MOVIE_NAME, data.name)
                startActivity(intent)
            }
        }
        adapter = EndlessAdapter(R.layout.genre_item
            ,paginationModel,onBind)
        listGenres.adapter  = adapter
        listGenres.setGridLayout(2)
        listGenres.addLoadMore(object :EndlessRV.OnLoadMore{

            override fun onLoadMore(currentPage: Int) {
                TODO("Not yet implemented")
            }
        })
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
        adapter.updateAdapterData(datas = genres)
        adapter.notifyDataSetChanged()
    }
}