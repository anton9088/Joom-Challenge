package com.joom.challenge.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Автоматически затирает значение перед вызовом Fragment.onDestroyView()
 */
class AutoClearedValue<T> : ReadWriteProperty<Fragment, T> {

    private var mValue: T? = null
    private var isObserverAdded = false

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return mValue
            ?: throw IllegalStateException("Value is not initialized or called after onDestroyView")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        if (!isObserverAdded) {
            thisRef.viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroyView() {
                    mValue = null
                    isObserverAdded = false
                }
            })

            isObserverAdded = true
        }

        this.mValue = value
    }
}