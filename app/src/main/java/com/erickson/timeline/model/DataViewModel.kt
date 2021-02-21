package com.erickson.timeline.model

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import java.util.*

class DataViewModel : ViewModel(), TimelineDataModel {
    override val allViewData: LiveData<Map<String, ViewData>> = SmithsonianLiveData(RequestHandlerImpl)
    override val timelineViewData: LiveData<List<ActiveViewData>> = ActiveViewLiveData(allViewData, RequestHandlerImpl)
    override var selectedId: String = ""
}