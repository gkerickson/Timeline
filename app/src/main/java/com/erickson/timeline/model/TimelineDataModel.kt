package com.erickson.timeline.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

interface TimelineDataModel {
    val timelineViewData: List<LiveData<ActiveViewData>>
    val choiceOneViewData: LiveData<ActiveViewData>
    val choiceTwoViewData: LiveData<ActiveViewData>
    val selectedId: MutableLiveData<String>
    val selected: MediatorLiveData<ActiveViewData>
}