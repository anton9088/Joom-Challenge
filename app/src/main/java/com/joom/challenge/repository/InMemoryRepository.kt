package com.joom.challenge.repository

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.CopyOnWriteArrayList

open class InMemoryRepository<T> : Repository<T> {

    private val publishSubject = BehaviorSubject.create<List<T>>()
    protected val items = CopyOnWriteArrayList<T>()

    override fun findAllObservable(): Observable<List<T>> {
        return publishSubject
    }

    override fun findFirst(): T? {
        return items.firstOrNull()
    }

    override fun findLast(): T? {
        return items.lastOrNull()
    }

    override fun addAll(newItems: List<T>) {
        items.addAll(newItems)
        publish()
    }

    override fun addAll(index: Int, newItems: List<T>) {
        items.addAll(index, newItems)
        publish()
    }

    override fun replaceAll(newItems: List<T>) {
        items.clear()
        items.addAll(newItems)
        publish()
    }

    override fun count(): Int {
        return items.size
    }

    private fun publish() {
        publishSubject.onNext(items.toList())
    }
}