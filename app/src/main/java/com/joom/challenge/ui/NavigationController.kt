package com.joom.challenge.ui

import android.app.Activity
import com.joom.challenge.ui.imageDetails.ImageDetailsActivity
import javax.inject.Inject

class NavigationController @Inject constructor(private val activity: Activity) {

    fun openImageDetailsActivity(imageId: String) {
        activity.startActivity(
            ImageDetailsActivity.newIntent(imageId, activity)
        )
    }
}