package com.hazard.movielist.data.model

import com.google.gson.annotations.SerializedName


public data class Genre(
    @SerializedName("genres")
    val genres : MutableList<GenreItem>

)

public data class GenreItem(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String  = ""
)