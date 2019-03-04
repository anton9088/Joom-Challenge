package com.joom.challenge.ui.imageDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gojuno.koptional.toOptional
import com.joom.challenge.model.Image
import com.joom.challenge.model.ImagePublication
import com.joom.challenge.model.User
import com.joom.challenge.services.ImageService
import com.joom.challenge.ui.NetworkRequestState
import com.joom.challenge.util.ErrorDescriptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImageDetailsViewModel @Inject constructor(
    private val imageService: ImageService,
    private val errorDescriptions: ErrorDescriptions
) : ViewModel() {

    private val loadImageState = MutableLiveData<NetworkRequestState>()
    private val image = MutableLiveData<Image>()
    private val authorName = MutableLiveData<String?>()
    private val authorProfileUrl = MutableLiveData<String?>()
    private val hashTags = MutableLiveData<String?>()

    private val disposables = CompositeDisposable()
    private var imageId: String = ""

    fun getImage(): LiveData<Image> = image
    fun getLoadImageState(): LiveData<NetworkRequestState> = loadImageState
    fun getAuthorName(): LiveData<String?> = authorName
    fun getAuthorProfileUrl(): LiveData<String?> = authorProfileUrl
    fun getHashTags(): LiveData<String?> = hashTags

    fun initWithImageId(imageId: String) {
        if (this.imageId == imageId) return

        this.imageId = imageId

        fetchFromRepository(imageId)
    }

    fun onRetryClicked() {
        loadFromNetwork(imageId)
    }

    private fun fetchFromRepository(imageId: String) {
        val disposable =
            Observable.fromCallable { imageService.getImageByIdFromRepository(imageId).toOptional() }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val imagePublication = it.toNullable()
                    if (imagePublication != null) {
                        updateModel(imagePublication)
                    } else {
                        loadFromNetwork(imageId)
                    }
                }

        disposables.add(disposable)
    }

    private fun loadFromNetwork(imageId: String) {
        loadImageState.value = NetworkRequestState.loading()

        val disposable = Observable.fromCallable { imageService.loadImageById(imageId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateModel(it)
                    loadImageState.value = NetworkRequestState.complete()
                },
                {
                    val errorDescription = errorDescriptions.getErrorDescription(it)
                    loadImageState.value = NetworkRequestState.error(errorDescription)
                }
            )

        disposables.add(disposable)
    }

    private fun updateModel(imagePublication: ImagePublication) {
        image.value = imagePublication.images.w480

        authorName.value = imagePublication.user
            ?.let { makeAuthor(it) }

        authorProfileUrl.value = imagePublication.user?.profileUrl

        hashTags.value = imagePublication.slug
            ?.let { parseHashTags(it) }
            ?.let { list ->
                if (list.isNotEmpty()) list.joinToString(" ") { "#$it" }
                else null
            }
    }

    private fun makeAuthor(user: User): String? {
        if (user.name.isNotBlank() && user.displayName.isNotBlank()) {
            if (user.displayName != user.name) {
                return "by ${user.displayName} (${user.name})"
            }
        }

        val name = when {
            user.name.isNotBlank() -> user.name
            user.displayName.isNotBlank() -> user.displayName
            else -> null
        } ?: return null

        return "by $name"
    }

    private fun parseHashTags(text: String): List<String> {
        return text.split("-").let {
            it.take(it.size - 1)
        }
    }

    override fun onCleared() {
        disposables.dispose()

        super.onCleared()
    }
}