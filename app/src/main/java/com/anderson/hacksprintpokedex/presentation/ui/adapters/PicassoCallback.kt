package com.anderson.hacksprintpokedex.presentation.ui.adapters

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PicassoCallback(
    private val imageView: ImageView,
    private val fallbackUrl: String
) : Callback {
    override fun onSuccess() {}

    override fun onError(e: Exception?) {
        Picasso.get()
            .load(fallbackUrl)
            .into(imageView)
    }
}
