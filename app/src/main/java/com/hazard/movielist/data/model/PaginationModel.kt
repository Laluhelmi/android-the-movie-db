package com.hazard.movielist.data.model


data class PaginatonModel(
    var currentPage: Int,
    var totalPages : Int,
    var datas      : List<*>
)
