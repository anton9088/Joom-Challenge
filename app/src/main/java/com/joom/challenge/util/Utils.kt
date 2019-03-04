package com.joom.challenge.util

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun Context.inflater() = LayoutInflater.from(this)!!

fun <T> autoCleared() = AutoClearedValue<T>()

fun <T: Any?> Fragment.observe(liveData: LiveData<T>, function: (T) -> Unit) {
    liveData.observe(this.viewLifecycleOwner, Observer<T> {
        function.invoke(it)
    })
}