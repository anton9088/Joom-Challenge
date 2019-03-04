package com.joom.challenge.model

import com.joom.challenge.pagination.PaginationData

interface PaginationResponse<T> : GiphyResponse<List<T>>, PaginationData<T>