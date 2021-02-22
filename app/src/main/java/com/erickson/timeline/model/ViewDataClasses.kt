package com.erickson.timeline.model

import android.graphics.Bitmap
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import java.util.*

data class ViewData(
    val id: String,
    val imageUrl: String,
    val date: Date,
    val notes: List<RequestDefinitions.SearchData.ContentBody.FreeText.Note>,
)

data class ActiveViewData(
    val viewData: ViewData,
    val bitmap: Bitmap?
)
