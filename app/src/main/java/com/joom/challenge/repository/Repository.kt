package com.joom.challenge.repository

import io.reactivex.Observable

interface Repository<T> {

    fun findAllObservable(): Observable<List<T>>

    fun findFirst(): T?

    fun findLast(): T?

    fun addAll(newItems: List<T>)

    fun addAll(index: Int, newItems: List<T>)

    fun replaceAll(newItems: List<T>)

    fun count(): Int
}