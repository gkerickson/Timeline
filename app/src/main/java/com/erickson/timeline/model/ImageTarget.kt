package com.erickson.timeline.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class ImageTarget(private val callback: NotifyObserversCallback) : Target {
    var image: Bitmap? = null
        private set

    interface NotifyObserversCallback {
        fun notifyObservers()
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        callback.notifyObservers()
        image = bitmap
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        TODO("Not yet implemented")
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        TODO("Not yet implemented")
    }
}