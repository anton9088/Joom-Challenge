package com.joom.challenge

import com.joom.challenge.di.DaggerAppComponent
import com.joom.challenge.util.RxErrorHandler
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.reactivex.plugins.RxJavaPlugins
import javax.inject.Inject

class JoomChallengeApplication : DaggerApplication() {

    @Inject
    lateinit var rxErrorHandler: RxErrorHandler

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler(rxErrorHandler)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }
}