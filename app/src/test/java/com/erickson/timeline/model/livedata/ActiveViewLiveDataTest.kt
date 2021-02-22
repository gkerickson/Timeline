package com.erickson.timeline.model.livedata

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.erickson.timeline.TestHelpers
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.smithsonian.RequestHandler
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ActiveViewLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val mockRequestHandler: RequestHandler = mock()

    @Test
    fun testSetValueBroadcastsValue() {
        val mockViewData = TestHelpers.mockViewDataFactory()
        var activeViewData: ActiveViewData? = null

        val subject = ActiveViewLiveData(mockRequestHandler)
        subject.observeForever {
            activeViewData = it
        }

        subject.setValue(mockViewData)

        assertEquals(mockViewData, activeViewData?.viewData)
    }

    @Test
    fun testComparingActiveViewLiveDataValues() {
        val greaterActiveView: ActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply {
            setValue(
                TestHelpers.mockViewDataFactory(TestHelpers.mockDateFromYear(4000))
            )
        }
        val lesserActiveView: ActiveViewLiveData = ActiveViewLiveData(mockRequestHandler).apply {
            setValue(
                TestHelpers.mockViewDataFactory(TestHelpers.mockDateFromYear(1000))
            )
        }
        assert(greaterActiveView > lesserActiveView)
    }

    @Test
    fun testSetBitmapUpdatesValueWithBitmap() {
        val mockBitmap: Bitmap = mock()
        val subject = ActiveViewLiveData(mockRequestHandler).apply {
            observeForever {}
            setValue(
                TestHelpers.mockViewDataFactory(TestHelpers.mockDateFromYear(4000))
            )
            assertNull(value?.bitmap)
            this.OnImageLoadCallback().setBitmap(mockBitmap)
        }

        assertEquals(mockBitmap, subject.value?.bitmap)
    }

    @Test
    fun testSetValueCallsLoadImage() {
        val mockViewData = TestHelpers.mockViewDataFactory(TestHelpers.mockDateFromYear(4000))
        ActiveViewLiveData(mockRequestHandler).apply {
            observeForever {}
            setValue(mockViewData)
        }
        verify(mockRequestHandler).loadImage(eq(mockViewData.imageUrl), any())
    }
}


