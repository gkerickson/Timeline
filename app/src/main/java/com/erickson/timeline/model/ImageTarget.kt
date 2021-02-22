package com.erickson.timeline.model

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
        Log.e("GALEN", "LOADING SUCCESS!")
        callback.setBitmap(bitmap)
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        Log.e("GALEN", "LOADING FAILED!")
        TODO("Not yet implemented")
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        Log.e("GALEN", "LOADING SETUP!")
    }
}