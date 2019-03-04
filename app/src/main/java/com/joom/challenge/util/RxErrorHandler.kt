package com.joom.challenge.util

import android.util.Log
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.functions.Consumer
import javax.inject.Inject

class RxErrorHandler @Inject constructor() : Consumer<Throwable> {

    override fun accept(t: Throwable) {
        if (t is UndeliverableException && t.cause is InterruptedException) {
            return
        }

        Log.e(TAG, "rx error", t)
    }

    companion object {
        const val TAG = "RxErrorHandler"
    }
}