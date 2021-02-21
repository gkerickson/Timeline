package com.erickson.timeline.model

import androidx.lifecycle.MediatorLiveData
import com.erickson.timeline.model.DataViewModel.ActiveViewData
import com.squareup.picasso.Picasso

class ActiveViewLiveData(private val smithLiveData: SmithsonianLiveData) :
    MediatorLiveData<List<ActiveViewData>>() {
    inner class Callback : DataViewModel.ImageTarget.NotifyObserversCallback {
        override fun notifyObservers() {
            notifyObservers()
        }
    }

    override fun onActive() {
        super.onActive()
        if (this.value == null) {
            addSource(smithLiveData) { map ->
                map.values.toList().subList(0, 4).sortedBy { viewData ->
                    viewData.date
                }.map { viewData ->
                    ActiveViewData(
                        viewData,
                        DataViewModel.ImageTarget(Callback()).also {
                            Picasso.get().load(viewData.imageUrl).into(it)
                        })
                }
            }
        }
    }
}