package com.erickson.timeline.model.livedata

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.ImageTarget
import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.RequestHandler

class ActiveViewLiveData(
    private val handler: RequestHandler,
) : MutableLiveData<ActiveViewData>(), Comparable<ActiveViewLiveData> {
    private val target = ImageTarget(OnImageLoadCallback())

    inner class OnImageLoadCallback : ImageTarget.NotifyObserversCallback {
        override fun setBitmap(image: Bitmap?) {
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

    override fun compareTo(other: ActiveViewLiveData): Int {
        return ((this.value?.viewData?.date?.time ?: 0) - (other.value?.viewData?.date?.time
            ?: 0)).toInt()
    }
}
