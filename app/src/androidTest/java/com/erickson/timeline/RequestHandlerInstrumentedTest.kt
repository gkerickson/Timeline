package com.erickson.timeline

import com.erickson.timeline.TestHelpers.mockDateAsDate
import com.erickson.timeline.TestHelpers.mockNote
import com.erickson.timeline.TestHelpers.mockResource
import com.erickson.timeline.TestHelpers.mockSearchData
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.smithsonian.RequestHandlerImpl.parseDate
import com.erickson.timeline.smithsonian.RequestHandlerImpl.processSearchDataResponseBody
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.Body
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class RequestHandlerInstrumentedTest {
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
}