package com.hazard.movielist.ui.base

interface EndlessInterface <T>{

    fun addData(data : T)


}
data class PaginationModel<T>(
    var currentPage: Int = 1,
    var totalPages: Int = 1,
    var datas: List<T>
)