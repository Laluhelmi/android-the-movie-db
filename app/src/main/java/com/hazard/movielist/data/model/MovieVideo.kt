package com.hazard.movielist.data.model

import com.google.gson.annotations.SerializedName

data class MovieVideo (
    @SerializedName("key")
    val key : String
)

data class MovieVideoResult(
    @SerializedName("results")
    val result : List<MovieVideo>
)