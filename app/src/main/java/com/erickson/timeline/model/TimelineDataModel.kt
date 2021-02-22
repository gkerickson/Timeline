package com.erickson.timeline.model

import androidx.lifecycle.LiveData

interface TimelineDataModel {
    val timelineViewData: List<LiveData<ActiveViewData>>
    val choiceOneViewData: LiveData<ActiveViewData>
    val choiceTwoViewData: LiveData<ActiveViewData>
    var selectedId: String
    fun getSelected() : LiveData<ActiveViewData>
}