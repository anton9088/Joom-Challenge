package com.joom.challenge.ui.gallery

import android.app.Activity
import dagger.Module
import dagger.Provides

@Module
class GalleryModule {

    @Provides
    fun activity(fragment: GalleryFragment): Activity {
        return fragment.activity!!
    }
}