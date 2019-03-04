package com.joom.challenge.model

import com.joom.challenge.pagination.PaginationItem

data class ImagePublication(
    override val id: String,
    val slug: String?,
    val images: Images,
    val user: User?
) : PaginationItem