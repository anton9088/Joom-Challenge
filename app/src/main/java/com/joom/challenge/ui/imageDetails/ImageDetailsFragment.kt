package com.joom.challenge.ui.imageDetails

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.joom.challenge.R
import com.joom.challenge.databinding.ImageDetailsFragmentBinding
import com.joom.challenge.di.ViewModelFactory
import com.joom.challenge.ui.NetworkRequestState.Type.*
import com.joom.challenge.util.observe
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.image_details_fragment.*
import javax.inject.Inject

class ImageDetailsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: ImageDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageId = arguments!!.getString(IMAGE_ID)!!

        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ImageDetailsViewModel::class.java)

        viewModel.initWithImageId(imageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ImageDetailsFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ImageDetailsFragment.viewModel
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lceLayout.onRetryClickedListener = {
            viewModel.onRetryClicked()
        }

        observe(viewModel.getLoadImageState()) { state ->
            when (state.type) {
                LOADING -> {
                    lceLayout.showLoading()
                }
                ERROR -> {
                    lceLayout.showError(state.errorText!!)
                }
                COMPLETE -> {
                    // do nothing
                }
            }
        }

        observe(viewModel.getImage()) { image ->
            content.post {
                val aspectRatio =
                    if (image.width != null && image.height != null && image.width > 0 && image.height > 0)
                        image.width.toFloat() / image.height
                    else 1f

                val width = content.width
                val height = (width / aspectRatio).toInt()

                imageView.layoutParams.width = width
                imageView.layoutParams.height = height
                imageView.layoutParams = imageView.layoutParams

                Glide.with(this@ImageDetailsFragment)
                    .load(image.url)
                    .placeholder(ColorDrawable(ContextCompat.getColor(context!!, R.color.imagePlaceholderColor)))
                    .into(imageView)

                lceLayout.showContent()
            }
        }
    }

    companion object {
        const val IMAGE_ID = "image_id"

        fun newInstance(imageId: String): ImageDetailsFragment {
            return ImageDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(IMAGE_ID, imageId)
                }
            }
        }
    }
}