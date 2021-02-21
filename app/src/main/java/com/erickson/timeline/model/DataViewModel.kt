package com.erickson.timeline.model

import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import java.util.*

class DataViewModel : ViewModel() {
    data class ViewData(
        val id: String,
        val imageUrl: String,
        val date: Date,
        val notes: List<Note>,
    )

    data class ActiveViewData(
        val viewData: ViewData,
        val imageTarget: ImageTarget
    )

    val allViewData = SmithsonianLiveData(RequestHandlerImpl)
    val timelineViewData = ActiveViewLiveData(allViewData, RequestHandlerImpl)
    var selectedId: String = ""
}