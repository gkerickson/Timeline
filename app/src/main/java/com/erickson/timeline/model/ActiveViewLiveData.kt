package com.erickson.timeline.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.erickson.timeline.model.DataViewModel.ActiveViewData
import com.erickson.timeline.smithsonian.RequestHandler

class ActiveViewLiveData(
    private val smithLiveData: LiveData<Map<String, DataViewModel.ViewData>>,
    private val handler: RequestHandler,
) :
    MediatorLiveData<List<ActiveViewData>>() {
    inner class Callback : ImageTarget.NotifyObserversCallback {
        override fun notifyObservers() {
            notifyObservers()
        }
    }

    override fun onActive() {
        super.onActive()
        if (this.value == null) {
            addSource(smithLiveData) { map ->
                value = map.values.toList().run {
                    if(size < 4) this
                    else subList(0, 4)
                }.sortedBy { viewData ->
                    viewData.date
                }.map { viewData ->
                    ActiveViewData(
                        viewData,
                        ImageTarget(Callback()).also {
                            handler.loadImage(viewData.imageUrl, it)
                        })
                }
            }
        }
    }
}