package com.erickson.timeline.model

import androidx.lifecycle.LiveData

interface TimelineDataModel {
//    val allViewData: LiveData<MutableMap<String, ViewData>>
    val timelineViewData: List<LiveData<ActiveViewData>>
    val choiceOneViewData: LiveData<ActiveViewData>
    val choiceTwoViewData: LiveData<ActiveViewData>

    fun getSelected() : LiveData<ActiveViewData>

    var selectedId: String
}