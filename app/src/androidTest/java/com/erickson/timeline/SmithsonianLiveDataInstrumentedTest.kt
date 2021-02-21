package com.erickson.timeline

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erickson.timeline.TestHelpers.mockDateAsDate
import com.erickson.timeline.TestHelpers.mockNote
import com.erickson.timeline.TestHelpers.mockResource
import com.erickson.timeline.TestHelpers.mockSearchData
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.model.SmithsonianLiveData
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.mockito.stubbing.Answer

class SmithsonianLiveDataInstrumentedTest {
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