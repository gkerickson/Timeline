package com.erickson.timeline.model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.smithsonian.RequestHandler

class ActiveViewLiveData(
    private val handler: RequestHandler,
) : MutableLiveData<ActiveViewData>() {
    private val target = ImageTarget(OnImageLoadCallback())

    inner class OnImageLoadCallback : ImageTarget.NotifyObserversCallback {
        override fun setBitmap(image: Bitmap?) {
            Log.e("GALEN", "SETTING")
            value?.viewData?.let { viewData ->
                value = ActiveViewData(
                    viewData,
                    image
                )
            }
        }
    }

    fun setValue(viewData: ViewData) {
        value = ActiveViewData(viewData, null)
        handler.loadImage(viewData.imageUrl, target)
    }
}
