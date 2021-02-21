package com.erickson.timeline

import com.erickson.timeline.InstrumentedTestHelpers.mockDateAsDate
import com.erickson.timeline.InstrumentedTestHelpers.mockNote
import com.erickson.timeline.InstrumentedTestHelpers.mockResource
import com.erickson.timeline.InstrumentedTestHelpers.mockSearchData
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
        val expectedDate = "Sat Jan 01 00:00:00 CST 2000"

        val date: Date = parseDate(dateString) ?: Date()

        assertEquals(expectedDate, date.toString())
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