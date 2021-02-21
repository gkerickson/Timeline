package com.erickson.timeline.model

import androidx.lifecycle.LiveData

interface TimelineDataModel {
    val allViewData: LiveData<Map<String, ViewData>>

    val timelineViewData: LiveData<List<ActiveViewData>>

    var selectedId: String
}