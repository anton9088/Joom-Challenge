package com.joom.challenge.pagination

import android.util.Log
import androidx.annotation.WorkerThread
import com.joom.challenge.repository.Repository
import io.reactivex.Observable

abstract class PaginationService<T: PaginationItem>(
    private val repository: Repository<T>
) {

    @Volatile
    private var totalCount = 0

    var pageSize = DEFAULT_PAGE_SIZE

    abstract fun loadPaginationData(offset: Int, limit: Int): PaginationData<T>

    fun getRepositoryObservable(): Observable<List<T>> {
        return repository.findAllObservable()
    }

    fun getRepositoryItemsCount(): Int {
        return repository.count()
    }

    fun hasMoreItemsToLoad(): Boolean {
        return totalCount == 0 || repository.count() < totalCount
    }

    @WorkerThread
    @Synchronized
    fun reload() {
        val paginationData = loadPaginationData(0, pageSize)
        Log.i(TAG, "reload")

        repository.replaceAll(paginationData.data)
        totalCount = paginationData.paginationInfo.totalCount
    }

    @WorkerThread
    @Synchronized
    fun loadBefore() {
        val paginationData = loadPaginationData(0, pageSize)
        Log.i(TAG, "load before")

        val firstItem = repository.findFirst()

        checkNotNull(firstItem) { "You must fetch initial data first" }

        val anchorIndex = paginationData.data.indexOfLast {
            it.id == firstItem.id
        }

        if (anchorIndex != -1) {
            Log.i(TAG, "anchor index = $anchorIndex")

            if (anchorIndex > 0) {
                val newItems = paginationData.data.subList(0, anchorIndex)
                repository.addAll(0, newItems)
            } else {
                Log.i(TAG, "no new data before current")
            }
        } else {
            Log.w(TAG, "can't find anchor")

            repository.replaceAll(paginationData.data)
        }
    }

    @WorkerThread
    @Synchronized
    fun loadMore(): Boolean {
        if (!hasMoreItemsToLoad()) return false

        val lastItem = repository.findLast()
        checkNotNull(lastItem) { "You must fetch initial data first" }

        // при запросе новой порции данных, так же запрашиваем часть предыдущих элементов
        // чтобы найти последний элемент из предыдущей загрузки

        val newOffset = repository.count() - Math.max(pageSize / 6, 1)
        val paginationData = loadPaginationData(newOffset, pageSize)

        Log.i(TAG, "load more, offset = $newOffset")

        val anchorIndex = paginationData.data.indexOfFirst {
            it.id == lastItem.id
        }

        // находим в новых данных последний элемент, который у нас был ранее
        // и добавляем в список все элементы, которые находятся после него

        if (anchorIndex != -1) {
            Log.i(TAG, "anchor index = $anchorIndex")

            if (paginationData.data.size > anchorIndex + 1) {
                val newItems = paginationData.data.subList(anchorIndex + 1, paginationData.data.size)
                repository.addAll(newItems)
            }

            totalCount = paginationData.paginationInfo.totalCount

            return false
        } else {
            // если прошло слишком много времени с момента последней загрузки
            // и мы не можем найти элемент, с которого нужно начать добавление новых
            // то просто загружаем данные заново

            Log.w(TAG, "can't find anchor")

            reload()

            return true
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 30
        const val TAG = "PaginationService"
    }
}