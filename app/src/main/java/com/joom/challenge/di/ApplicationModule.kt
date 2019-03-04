package com.joom.challenge.di

import android.content.Context
import com.joom.challenge.JoomChallengeApplication
import dagger.Binds
import dagger.Module

@Module
interface ApplicationModule {

    @Binds
    fun applicationContext(application: JoomChallengeApplication): Context
}