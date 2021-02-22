package com.erickson.timeline.model.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erickson.timeline.TestHelpers
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.smithsonian.RequestHandler
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class HighestTimeLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockRequestHandler: RequestHandler = mock()

    @Test
    fun testIsSetupWithHighestValue() {
        val highestDate = TestHelpers.mockDateFromYear(2000)
        val otherDate = TestHelpers.mockDateFromYear(1000)

        val subject = HighestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(highestDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
        }

        assertEquals(highestDate, subject.value)
    }

    @Test
    fun testUpdatesWhenHigherValueAppears() {
        val highestDate = TestHelpers.mockDateFromYear(2000)
        val otherDate = TestHelpers.mockDateFromYear(1000)
        val higherDate = TestHelpers.mockDateFromYear(3000)

        val mockActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))}

        val subject = HighestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(highestDate))},
                mockActiveViewLiveData,
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
            mockActiveViewLiveData.value = ActiveViewData(
                TestHelpers.mockViewDataFactory(higherDate),
                null
            )
        }

        assertEquals(higherDate, subject.value)
    }

    @Test
    fun testUpdatesWhenHighestValueIsRemoved() {
        val highDate = TestHelpers.mockDateFromYear(2000)
        val otherDate = TestHelpers.mockDateFromYear(1000)
        val highestDate = TestHelpers.mockDateFromYear(3000)
        val mockActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(highestDate))}

        val subject = HighestTimeLiveData(
            listOf(
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
                mockActiveViewLiveData,
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(highDate))},
                ActiveViewLiveData(mockRequestHandler).apply{setValue(TestHelpers.mockViewDataFactory(otherDate))},
            )
        ).apply {
            observeForever {  }
            mockActiveViewLiveData.value = ActiveViewData(
                TestHelpers.mockViewDataFactory(otherDate),
                null
            )
        }

        assertEquals(highDate, subject.value)
    }
}