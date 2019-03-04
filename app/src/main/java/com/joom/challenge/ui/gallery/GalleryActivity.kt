package com.joom.challenge.ui.gallery

import android.os.Bundle
import com.joom.challenge.R
import dagger.android.support.DaggerAppCompatActivity

class GalleryActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gallery_activity)
    }
}
