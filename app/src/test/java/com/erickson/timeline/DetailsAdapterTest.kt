package com.erickson.timeline

import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.model.ViewData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test

class DetailsAdapterTest {

    @Test
    fun testGetItemCount() {
        val mockAllViewData = MutableLiveData<Map<String, ViewData>>(
            mutableMapOf<String, ViewData>().also { map ->
                listOf<ViewData>(
                    TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory()
                ).forEach {
                    map[it.id] = it
                }
            }
        )
        val mockViewModel: DataViewModel = mock()
        whenever(mockViewModel.allViewData).thenReturn(mockAllViewData)

        val subject = DetailsAdapter(mockViewModel)

        assertEquals(4, subject.itemCount)
    }
}