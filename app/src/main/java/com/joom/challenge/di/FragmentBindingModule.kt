package com.joom.challenge.di

import com.joom.challenge.ui.gallery.GalleryFragment
import com.joom.challenge.ui.gallery.GalleryModule
import com.joom.challenge.ui.imageDetails.ImageDetailsFragment
import com.joom.challenge.ui.imageDetails.ImageDetailsModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBindingModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = [GalleryModule::class])
    fun galleryFragment(): GalleryFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [ImageDetailsModule::class])
    fun imageDetailsFragment(): ImageDetailsFragment
}