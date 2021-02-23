package com.erickson.timeline.model.helpers

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class ImageTarget(private val callback: NotifyObserversCallback) : Target {
    interface NotifyObserversCallback {
        fun setBitmap(image: Bitmap?)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        callback.setBitmap(bitmap)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        Log.e("ImageTarget", "LOADING FAILED!")
    }

    override fun onPrepareLoad(placeHoldserDrawable: Drawable?) {}
}