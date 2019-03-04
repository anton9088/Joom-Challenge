package com.joom.challenge.di

import com.joom.challenge.JoomChallengeApplication
import com.joom.challenge.api.ApiModule
import com.joom.challenge.repository.RepositoryModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ApiModule::class,
        RepositoryModule::class,
        ActivityBindingModule::class,
        ViewModelBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<JoomChallengeApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<JoomChallengeApplication>()
}