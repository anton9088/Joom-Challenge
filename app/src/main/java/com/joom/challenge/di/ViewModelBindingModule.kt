package com.joom.challenge.di

import androidx.lifecycle.ViewModel
import com.joom.challenge.ui.gallery.GalleryViewModel
import com.joom.challenge.ui.imageDetails.ImageDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelBindingModule {

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    fun galleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ImageDetailsViewModel::class)
    fun imageDetailsViewModel(imageDetailsViewModel: ImageDetailsViewModel): ViewModel
}