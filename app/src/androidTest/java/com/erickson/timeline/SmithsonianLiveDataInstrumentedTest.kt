package com.erickson.timeline

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.model.SmithsonianLiveData
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import com.erickson.timeline.smithsonian.RequestHandlerImpl.parseDate
import com.erickson.timeline.smithsonian.RequestHandlerImpl.processSearchDataResponseBody
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.Body
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.stubbing.Answer
import java.util.*

class SmithsonianLiveDataInstrumentedTest {
    @Test
    fun testParseDate() {
        val dateString = "2000s"

        val date: Date = parseDate(dateString)
        val expectedDate = Calendar.getInstance().run {
            set(2000, 0, 1, 0, 0, 0)
            time
        }

        assertEquals(expectedDate.toString(), date.toString())
    }

    var smith: Smithsonian = mock<Smithsonian>()

    private val mockResource =
        RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody.Media.Resource(
            RequestDefinitions.ImageType.THUMBNAIL.type,
            "mockResourceUrl"
        )

    private val mockMedia = RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody.Media(
        "mockGuid",
        listOf(mockResource)
    )

    private val mockDate: String = "2000s"
    private val mockDateAsDate = Calendar.getInstance().run {
        set(2000, 0, 1, 0, 0, 0)
        time
    }

    private val mockNote = RequestDefinitions.SearchData.ContentBody.FreeText.Note(
        "TEST LABEL",
        "TEST CONTENT"
    )

    private val mockSearchData = RequestDefinitions.SearchData(
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

    @Test
    fun testProcessEmptySearchDataResponse() {
        val mockBody: Body<RequestDefinitions.SearchData> =
            Body(200, 0, Body.Response(listOf()))

        val mapOut = processSearchDataResponseBody(mockBody)

        assert(mapOut.isEmpty())
    }

    @Test
    fun testProcessSearchDataResponse() {
        val mockBody: Body<RequestDefinitions.SearchData> = Body(
            200, 0, Body.Response(
                listOf(mockSearchData)
            )
        )

        val mapOut = processSearchDataResponseBody(mockBody)

        val mockViewData = DataViewModel.ViewData(
            mockSearchData.id,
            mockResource.url,
            mockDateAsDate,
            listOf(mockNote)
        )

        assertEquals(mapOf(Pair(mockViewData.id, mockViewData)).toString(), mapOut.toString())
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testSmithsonianLiveData() {
        val mockViewData = DataViewModel.ViewData(
            mockSearchData.id,
            mockResource.url,
            mockDateAsDate,
            listOf(mockNote)
        )

        val mockViewDataMap = mapOf(Pair(mockViewData.id, mockViewData))

        val handler: RequestHandler = mock {
            on { getData(any()) } doAnswer Answer<Void> { invocation ->
                val callback: RequestHandlerImpl.DataRequestCallback =
                    invocation!!.arguments[0] as RequestHandlerImpl.DataRequestCallback
                callback.withData(mockViewDataMap)
                null
            }
        }

        val smithLiveData = SmithsonianLiveData(handler)

        assertNull(smithLiveData.value)
        smithLiveData.observeForever {}
        assertEquals(mockViewDataMap.toString(), smithLiveData.value.toString())
    }
}