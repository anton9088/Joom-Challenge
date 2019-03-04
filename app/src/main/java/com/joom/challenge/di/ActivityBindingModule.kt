package com.joom.challenge.di

import com.joom.challenge.ui.gallery.GalleryActivity
import com.joom.challenge.ui.imageDetails.ImageDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    fun galleryActivity(): GalleryActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [FragmentBindingModule::class])
    fun imageDetailsActivity(): ImageDetailsActivity
}