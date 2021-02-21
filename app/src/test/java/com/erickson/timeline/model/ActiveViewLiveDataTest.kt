package com.erickson.timeline.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.TestHelpers
import com.erickson.timeline.smithsonian.RequestHandler
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Before
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
            TestHelpers.mockResource.url + id.toString(),
            TestHelpers.mockDateFromYear(2000 + id),
            listOf(TestHelpers.mockNote)
        )
        id++
        return Pair(viewData.id, viewData)
    }

    private lateinit var mockLiveData: MutableLiveData<Map<String, DataViewModel.ViewData>>
    private var mockHandler: RequestHandler = mock()
    private lateinit var subject: ActiveViewLiveData

    @Before
    fun setup() {
        mockLiveData = MutableLiveData<Map<String, DataViewModel.ViewData>>()
        subject = ActiveViewLiveData(mockLiveData, mockHandler)
        subject.observeForever {}
    }

    @Test
    fun testRespondsToDependentLiveDataUpdates() {
        val mockMap = mutableMapOf(
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
        )

        mockLiveData.value = mockMap

        val expectedViewData = mockMap.map { it.value }
        val subjectViewData = subject.value?.map { it.viewData }

        assertEquals(expectedViewData.toString(), subjectViewData.toString())
    }

    @Test
    fun testHandlesToFewObjectsGracefully() {
        val mockMap = mutableMapOf(
            mockViewDataPairFactory()
        )

        mockLiveData.value = mockMap

        val expectedViewData = mockMap.map { it.value }
        val subjectViewData = subject.value?.map { it.viewData }

        assertEquals(expectedViewData.toString(), subjectViewData.toString())
    }

    private val firstViewDataPair = DataViewModel.ViewData(
        "firstViewData",
        "TEMP",
        TestHelpers.mockDateFromYear(1),
        emptyList()
    ).run {
        Pair(this.id, this)
    }

    @Test
    fun testSortsValuesInList() {

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

        assertEquals(
            firstViewDataPair.second.toString(),
            subject.value?.get(0)?.viewData.toString()
        )
        assertEquals(lastViewDataPair.second.toString(), subject.value?.last()?.viewData.toString())
    }

    @Test
    fun testRequestHandlerIsGettingCalledWithTargetAndImageUrl() {
        val mockMap = mutableMapOf(
            mockViewDataPairFactory(),
            firstViewDataPair,
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
            mockViewDataPairFactory(),
        )

        mockLiveData.value = mockMap

        subject.value?.forEach {
            verify(mockHandler).loadImage(
                eq(it.viewData.imageUrl),
                any()
            )
        }
        assertEquals(
            firstViewDataPair.second.toString(),
            subject.value?.get(0)?.viewData.toString()
        )
    }
}