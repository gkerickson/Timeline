package com.erickson.timeline

import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import java.util.*

object TestHelpers {
    val mockResource =
        RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody.Media.Resource(
            RequestDefinitions.ImageType.THUMBNAIL.type,
            "mockResourceUrl"
        )

    val mockMedia = RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody.Media(
        "mockGuid",
        listOf(mockResource)
    )

    val mockDate: String = "2000s"

    fun mockDateFromYear(year: Int): Date {
        return Calendar.getInstance().run {
            set(year, 0, 1, 0, 0, 0)
            time
        }
    }

    val mockDateAsDate = Calendar.getInstance().run {
        set(2000, 0, 1, 0, 0, 0)
        time
    }

    val mockNote = RequestDefinitions.SearchData.ContentBody.FreeText.Note(
        "TEST LABEL",
        "TEST CONTENT"
    )

    val mockSearchData = RequestDefinitions.SearchData(
        "testSearchId",
        "testTitle",
        RequestDefinitions.SearchData.ContentBody(
            RequestDefinitions.SearchData.ContentBody.Record(
                "recordId",
                RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody(
                    listOf(mockMedia)
                )
            ),
            RequestDefinitions.SearchData.ContentBody.Structured(
                listOf(mockDate),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList()
            ),
            RequestDefinitions.SearchData.ContentBody.FreeText(listOf(mockNote), null)
        )
    )

    private var viewDataIndex = 1
    fun mockViewDataFactory(): ViewData {
        return ViewData(
            "ViewDataId $viewDataIndex",
            "ImageUrl $viewDataIndex",
            mockDateFromYear(2000 + viewDataIndex),
            emptyList()
        ).also {
            viewDataIndex++
        }
    }
}
