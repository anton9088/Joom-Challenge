package com.joom.challenge.ui.imageDetails

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
class ImageDetailsModule {

    @Provides
    fun activity(fragment: ImageDetailsFragment): Activity {
        return fragment.activity!!
    }
}