package com.joom.challenge.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val providersMap: Map<
            Class<out ViewModel>,
            @JvmSuppressWildcards Provider<ViewModel>
            >
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val provider = providersMap[modelClass]
            ?: throw IllegalStateException("Unknown view model class: $modelClass")

        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}