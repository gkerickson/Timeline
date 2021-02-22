package com.erickson.timeline.model

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import com.erickson.timeline.smithsonian.RequestHandler

class ActiveViewLiveData(
    private val handler: RequestHandler,
) : LiveData<ActiveViewData>() {
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
        handler.loadImage(viewData.imageUrl, target)
        value = ActiveViewData(viewData, null)
    }
}
