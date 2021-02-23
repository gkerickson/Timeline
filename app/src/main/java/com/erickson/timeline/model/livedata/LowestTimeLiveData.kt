package com.erickson.timeline.model.livedata

import androidx.lifecycle.MediatorLiveData
import dagger.hilt.android.scopes.ViewModelScoped
import java.util.*
import javax.inject.Inject

@ViewModelScoped
class LowestTimeLiveData @Inject constructor(private val timelineViewData: List<ActiveViewLiveData>) : MediatorLiveData<Date>() {
    init {
        timelineViewData.forEach { timelineLiveData ->
            addSource(timelineLiveData) {
                value = Collections.min(timelineViewData.mapNotNull { it.value?.viewData?.date })
            }
        }
    }
}