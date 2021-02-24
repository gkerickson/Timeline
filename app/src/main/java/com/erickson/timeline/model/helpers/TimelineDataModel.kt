package com.erickson.timeline.model.helpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewData

interface TimelineDataModel {
    val timelineViewData: List<LiveData<ActiveViewData>>
    val choiceOneViewData: LiveData<ActiveViewData>
    val choiceTwoViewData: LiveData<ActiveViewData>
    val selectedId: MutableLiveData<String>
    val selected: MutableLiveData<ActiveViewData>
}