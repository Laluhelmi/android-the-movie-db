package com.hazard.movielist.data.model

import com.google.gson.annotations.SerializedName


data class Movie(
    @SerializedName("id")
    val id : Int = 0,

    @SerializedName("original_title")
    val original_title : String = "",

    @SerializedName("overview")
    val overview : String = "",

    @SerializedName("vote_average")
    val popularity : Double = 0.0,

    @SerializedName("title")
    val title : String = "",

    @SerializedName("poster_path")
    val poster_path : String = "",

    @SerializedName("homepage")
    val homepage : String = "",


    @SerializedName("release_date")
    val release_date : String = ""

)