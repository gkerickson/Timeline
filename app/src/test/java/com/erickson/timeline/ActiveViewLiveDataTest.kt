package com.erickson.timeline

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewLiveData
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.smithsonian.RequestHandler
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ActiveViewLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private var id = 0

    private fun mockViewDataPairFactory(): Pair<String, DataViewModel.ViewData> {
        val viewData = DataViewModel.ViewData(
            TestHelpers.mockSearchData.id + id.toString(),
            TestHelpers.mockResource.url,
            TestHelpers.mockDateFromYear(2000 + id),
            listOf(TestHelpers.mockNote)
        )
        id++
        return Pair(viewData.id, viewData)
    }

    @Test
    fun testRespondsToDependentLiveDataUpdates() {
        val mockLiveData = MutableLiveData<Map<String, DataViewModel.ViewData>>()

        val handler: RequestHandler = mock()
        val subject = ActiveViewLiveData(mockLiveData, handler)

        mockLiveData.observeForever {}
        subject.observeForever {}

        assertNull(subject.value)

        val mockMap = mutableMapOf(
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
        )

        mockLiveData.value = mockMap

        val expectedViewData = mockMap.map {
            it.value
        }

        val subjectViewData = subject.value?.map {
            it.viewData
        }

        assertEquals(expectedViewData.toString(), subjectViewData.toString())
    }

    @Test
    fun testHandlesToFewObjectsGracefully() {
        val mockLiveData = MutableLiveData<Map<String, DataViewModel.ViewData>>()

        val handler: RequestHandler = mock()
        val subject = ActiveViewLiveData(mockLiveData, handler)

        mockLiveData.observeForever {}
        subject.observeForever {}

        assertNull(subject.value)

        val mockMap = mutableMapOf(
            mockViewDataPairFactory(),
        )

        mockLiveData.value = mockMap

        val expectedViewData = mockMap.map {
            it.value
        }

        val subjectViewData = subject.value?.map {
            it.viewData
        }

        assertEquals(expectedViewData.toString(), subjectViewData.toString())
    }

    @Test
    fun testSortsValuesInList() {
        val mockLiveData = MutableLiveData<Map<String, DataViewModel.ViewData>>()

        val handler: RequestHandler = mock()
        val subject = ActiveViewLiveData(mockLiveData, handler)

        mockLiveData.observeForever {}
        subject.observeForever {}

        assertNull(subject.value)

        val firstViewDataPair = DataViewModel.ViewData(
            "firstViewData",
            "TEMP",
            TestHelpers.mockDateFromYear(1),
            emptyList()
        ).run {
            Pair(this.id, this)
        }
        val lastViewDataPair = DataViewModel.ViewData(
            "lastViewData",
            "TEMP",
            TestHelpers.mockDateFromYear(3000),
            emptyList()
        ).run {
            Pair(this.id, this)
        }

        val mockMap = mutableMapOf(
            mockViewDataPairFactory(),
            firstViewDataPair,
            lastViewDataPair,
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
        )

        mockLiveData.value = mockMap

        assertEquals(firstViewDataPair.second.toString(), subject.value?.get(0)?.viewData.toString())
        assertEquals(lastViewDataPair.second.toString(), subject.value?.last()?.viewData.toString())
    }

    @Test
    fun testRequestHandlerIsGettingCalledWithTarget() {

    }
}