package com.joom.challenge.pagination

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joom.challenge.ui.NetworkRequestState
import com.joom.challenge.util.ErrorDescriptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class PaginationViewModel<T : PaginationItem>(
    private val service: PaginationService<T>,
    private val errorDescriptions: ErrorDescriptions
) : ViewModel() {

    private val data = MutableLiveData<List<T>>()
    private val loadInitialState = MutableLiveData<NetworkRequestState>()
    private val loadMoreState = MutableLiveData<NetworkRequestState>()
    private val refreshState = MutableLiveData<NetworkRequestState>()

    private var stopLoadMoreByScroll = false
    private var loadMoreDisposable: Disposable? = null
    private var disposables = CompositeDisposable()

    fun getData(): LiveData<List<T>> = data
    fun getLoadInitialState(): LiveData<NetworkRequestState> = loadInitialState
    fun getLoadMoreState(): LiveData<NetworkRequestState> = loadMoreState
    fun getRefreshState(): LiveData<NetworkRequestState> = refreshState

    init {
        service.pageSize = getPageSize()

        subscribeOnRepositoryChanges()

        if (service.getRepositoryItemsCount() == 0) {
            loadInitial()
        } else {
            loadBefore()
        }
    }

    abstract fun getPageSize(): Int

    abstract fun getPaginationThreshold(): Int

    fun onRefresh() {
        cancelLoadMoreIfNeeded()
        refresh()
    }

    fun onLoadMoreClicked() {
        loadMore()
    }

    fun onRetryClicked() {
        loadInitial()
    }

    fun onListScrolled(lastVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoadingMore()) return
        if (stopLoadMoreByScroll) return

        if (totalItemCount - lastVisibleItemPosition < getPaginationThreshold()) {
            loadMore()
        }
    }

    private fun subscribeOnRepositoryChanges() {
        val disposable = service.getRepositoryObservable()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                data.value = it
            }

        disposables.add(disposable)
    }

    private fun loadInitial() {
        loadInitialState.value = NetworkRequestState.loading()

        val disposable = Observable.fromCallable { service.reload() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    loadInitialState.value = NetworkRequestState.complete()
                },
                {
                    Log.e(TAG, "reload error", it)

                    val errorDescription = errorDescriptions.getErrorDescription(it)
                    loadInitialState.value = NetworkRequestState.error(errorDescription)
                })

        disposables.add(disposable)
    }

    private fun refresh() {
        refreshState.value = NetworkRequestState.loading()

        val disposable = Observable.fromCallable { service.reload() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    refreshState.value = NetworkRequestState.complete()
                    stopLoadMoreByScroll = false
                },
                {
                    Log.e(TAG, "reload error", it)

                    val errorDescription = errorDescriptions.getErrorDescription(it)
                    refreshState.value = NetworkRequestState.error(errorDescription)
                })

        disposables.add(disposable)
    }

    private fun loadBefore() {
        val disposable = Observable.fromCallable { service.loadBefore() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {

                },
                {
                    Log.e(TAG, "load before error", it)

                    // не показываем ошибку, т.к. этот запрос не инициирован пользователем
                })

        disposables.add(disposable)
    }

    private fun loadMore() {
        if (!service.hasMoreItemsToLoad()) return

        loadMoreState.value = NetworkRequestState.loading()

        val disposable = Observable.fromCallable { service.loadMore() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {
                loadMoreDisposable = null
            }
            .subscribe(
                {
                    loadMoreState.value = NetworkRequestState.complete(isDataOutdated = it)
                    stopLoadMoreByScroll = false
                },
                {
                    Log.e(TAG, "load more error", it)

                    val errorDescription = errorDescriptions.getErrorDescription(it)
                    loadMoreState.value = NetworkRequestState.error(errorDescription)

                    stopLoadMoreByScroll = true
                })

        loadMoreDisposable = disposable
        disposables.add(disposable)
    }

    private fun cancelLoadMoreIfNeeded() {
        if (loadMoreDisposable != null) {
            loadMoreDisposable?.dispose()
            loadMoreDisposable = null
        }
    }

    private fun isLoadingMore(): Boolean {
        return loadMoreDisposable != null
    }

    override fun onCleared() {
        disposables.dispose()

        super.onCleared()
    }

    companion object {
        const val TAG = "PaginationViewModel"
    }
}