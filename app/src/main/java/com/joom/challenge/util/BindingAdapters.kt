package com.joom.challenge.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@BindingAdapter(value = ["app:imageUrl", "app:placeholder"], requireAll = false)
fun loadImage(view: ImageView, url: String?, placeHolder: Drawable?) {
    Glide.with(view)
        .load(url)
        .placeholder(placeHolder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}