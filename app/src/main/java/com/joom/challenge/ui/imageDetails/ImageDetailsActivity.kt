package com.joom.challenge.ui.imageDetails

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.NavUtils
import com.joom.challenge.R
import dagger.android.support.DaggerAppCompatActivity

class ImageDetailsActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_details_activity)

        if (supportFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            val imageId = getImageId()
            val fragment = ImageDetailsFragment.newInstance(imageId)

            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.image)
        }
    }

    private fun getImageId(): String {
        val deepLink = intent?.data

        return if (deepLink != null) {
            getImageIdFromDeepLink(deepLink)
        } else {
            intent.getStringExtra(IMAGE_ID)!!
        }
    }

    private fun getImageIdFromDeepLink(uri: Uri): String {
        return uri.pathSegments
            .last()
            .split("-")
            .last()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if (isTaskRoot) {
            overridePendingTransition(0, 0)

            val upIntent = NavUtils.getParentActivityIntent(this)!!
            startActivity(upIntent)
        }
    }

    companion object {
        const val IMAGE_ID = "image_id"

        fun newIntent(imageId: String, context: Context): Intent {
            return Intent(context, ImageDetailsActivity::class.java).apply {
                putExtra(IMAGE_ID, imageId)
            }
        }
    }
}