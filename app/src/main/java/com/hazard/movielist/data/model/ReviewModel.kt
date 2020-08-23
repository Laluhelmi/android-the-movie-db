package com.hazard.movielist.data.model

import com.google.gson.annotations.SerializedName

data class ReviewModel (
    @SerializedName("author")
    val author : String = "",
    @SerializedName("content")
    val content : String = ""

)

data class ReviewResult(
    @SerializedName("page")
    val page : Int,
    @SerializedName("total_pages")
    val total_pages : Int,
    @SerializedName("results")
    val result : List<ReviewModel>
)