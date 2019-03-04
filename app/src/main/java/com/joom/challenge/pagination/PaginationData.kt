package com.joom.challenge.pagination

import com.joom.challenge.model.PaginationInfo

interface PaginationData<T> {
    val data: List<T>
    val paginationInfo: PaginationInfo
}