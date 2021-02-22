package com.erickson.timeline.model.livedata

import androidx.lifecycle.MediatorLiveData
import java.util.*

class LowestTimeLiveData(private val timelineViewData: List<ActiveViewLiveData>) : MediatorLiveData<Date>() {
    override fun onActive() {
        timelineViewData.forEach { timelineLiveData ->
            addSource(timelineLiveData) {
                value = Collections.min(timelineViewData.mapNotNull { it.value?.viewData?.date })
            }
        }
        super.onActive()
    }
}