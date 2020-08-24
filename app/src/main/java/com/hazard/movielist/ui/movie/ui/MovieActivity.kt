package com.hazard.movielist.ui.movie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hazard.movielist.R
import com.hazard.movielist.data.api.ApiHelper
import com.hazard.movielist.data.api.ApiServiceImpl
import com.hazard.movielist.data.model.Movie
import com.hazard.movielist.ui.base.*
import com.hazard.movielist.ui.movie.adapter.MovieAdapter
import com.hazard.movielist.ui.movie.viewmodel.MovieViewModel
import com.hazard.movielist.util.Constants
import com.mindorks.framework.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.loading
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.genre_item.view.*

class MovieActivity : BaseActivity(),MovieAdapter.OnItemClick {

    var genreId : Int = 0
    var currentPage   = 1
    var totalPage     = 0

    lateinit var adapter:EndlessAdapter<Movie>


    lateinit var viewModel : MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        getIntentId()
        setUpViewModel()
        setAdapter()
        getLiveData()
    }

    fun getIntentId(){
        this.genreId  = intent.getIntExtra(Constants.INTENT_GENRE_ID,0)
        val pageTitle = intent.getStringExtra(Constants.INTENT_MOVIE_NAME)
        supportActionBar?.title = pageTitle
    }

    fun setUpViewModel(){
        val apiSource = ApiHelper(ApiServiceImpl())
        viewModel = ViewModelProviders.of(this, ViewModelFactory(apiSource))
            .get(MovieViewModel::class.java)
    }

    override fun onItemClick(movie: Movie) {
        val intent = Intent(this,MovieDetailActivity::class.java)
        intent.putExtra(Constants.INTENT_MOVIE_ID,movie.id)
        intent.putExtra(Constants.INTENT_MOVIE_NAME,movie.title)
        startActivity(intent)
    }

    fun setAdapter(){
        var paginationModel = PaginationModel<Movie>(datas = arrayListOf())
        val onBind = object: EndlessAdapter.BindViewHolder<Movie>{
            override fun bind(itemView: View, movie: Movie) {
              //  holder.title.text = data.name
                itemView.title.text = movie.title
                itemView.item_image.visibility = View.VISIBLE
                val options = RequestOptions()
                options.fitCenter()
                Glide.with(itemView.item_image.context)
                    .load(Constants.TMB_IMAGE_HOST+"${movie.poster_path}")
                    .apply(options)
                    .into(itemView.item_image)
            }
            override fun onItemClick(movie: Movie) {
                val intent = Intent(applicationContext,MovieDetailActivity::class.java)
                intent.putExtra(Constants.INTENT_MOVIE_ID,movie.id)
                intent.putExtra(Constants.INTENT_MOVIE_NAME,movie.title)
                startActivity(intent)
            }
        }
        adapter = EndlessAdapter(R.layout.genre_item
            ,paginationModel,onBind)
        listMovie.adapter  = adapter
        listMovie.setGridLayout(2)
        listMovie.addLoadMore(object : EndlessRV.OnLoadMore{
            override fun onLoadMore(currentPage: Int) {
                viewModel.loadMore(genreId,currentPage)
            }
        })
    }

    fun getLiveData(){
        viewModel.fetchMovieByGenre(genreId = genreId)
        viewModel.getMovies().observe(this, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    loading.visibility = View.GONE
                    it.data?.let {
                        movieByGenreModel ->
                        val totalPage   = movieByGenreModel.total_pages
                        val currentPage = movieByGenreModel.page

                        renderItem(currentPage,totalPage,movieByGenreModel.result)
                    }
                }
                Status.ERROR ->{
                    loading.visibility = View.GONE
                    it?.message?.let {
                        showToast(it)
                    }
                }
            }
        })
    }

    fun renderItem(page: Int,totalPages : Int,movie: List<Movie>){
        adapter.updateAdapterData(datas = movie,page = page,totalPage = totalPages)
        adapter.notifyDataSetChanged()

    }
}