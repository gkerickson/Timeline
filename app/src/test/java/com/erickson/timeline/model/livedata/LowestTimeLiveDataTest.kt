package com.erickson.timeline.model.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erickson.timeline.TestHelpers
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.smithsonian.RequestHandler
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class LowestTimeLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockRequestHandler: RequestHandler = mock()

    @Test
    fun testIsSetupWithHighestValue() {
        val lowestDate = TestHelpers.mockDateFromYear(1000)
        val otherDate = TestHelpers.mockDateFromYear(2000)

        val subject = LowestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(lowestDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
        }

        assertEquals(lowestDate, subject.value)
    }

    @Test
    fun testUpdatesWhenHigherValueAppears() {
        val lowestDate = TestHelpers.mockDateFromYear(1000)
        val otherDate = TestHelpers.mockDateFromYear(2000)
        val lowerDate = TestHelpers.mockDateFromYear(500)

        val mockActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply{setValue(
            TestHelpers.mockViewDataFactory(otherDate))}

        val subject = LowestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(lowestDate))},
                mockActiveViewLiveData,
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
            mockActiveViewLiveData.value = ActiveViewData(
                TestHelpers.mockViewDataFactory(lowerDate),
                null
            )
        }

        assertEquals(lowerDate, subject.value)
    }

    @Test
    fun testUpdatesWhenHighestValueIsRemoved() {
        val lowDate = TestHelpers.mockDateFromYear(1000)
        val otherDate = TestHelpers.mockDateFromYear(2000)
        val lowestDate = TestHelpers.mockDateFromYear(500)
        val mockActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply{setValue(
            TestHelpers.mockViewDataFactory(lowestDate))}

        val subject = LowestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                mockActiveViewLiveData,
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(lowDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
            mockActiveViewLiveData.value = ActiveViewData(
                TestHelpers.mockViewDataFactory(otherDate),
                null
            )
        }

        assertEquals(lowDate, subject.value)
    }
}