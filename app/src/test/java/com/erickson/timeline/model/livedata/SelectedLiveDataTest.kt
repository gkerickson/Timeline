package com.erickson.timeline.model.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.TestHelpers.mockActiveViewDataFactory
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SelectedLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    fun mockTimelineFactory(
        id1: String = "timelineId1",
        id2: String = "timelineId2"
    ): List<ActiveViewLiveData> = listOf(
        mock { on { value } doReturn mockActiveViewDataFactory(id1) },
        mock { on { value } doReturn mockActiveViewDataFactory(id2) }
    )

    fun mockActiveViewLiveDataFactory(
        id: String? = null,
    ): ActiveViewLiveData = mock { on { value } doReturn mockActiveViewDataFactory(id) }

    @Test
    fun testSelectedIsChoiceOneWhenIdsMatch() {
        val mockChoiceOneId = "matches"
        val mockChoiceOne = mockActiveViewLiveDataFactory(mockChoiceOneId)

        val subject = SelectedLiveData(
            MutableLiveData(mockChoiceOneId),
            mockTimelineFactory(),
            mockChoiceOne,
            mockActiveViewLiveDataFactory(),
        )
        subject.observeForever {}

        assertEquals(mockChoiceOne.value, subject.value)
    }

    @Test
    fun testSelectedIsChoiceTwoWhenIdsMatch() {
        val mockChoiceTwoId = "matches"
        val mockChoiceTwo = mockActiveViewLiveDataFactory(mockChoiceTwoId)

        val subject = SelectedLiveData(
            MutableLiveData(mockChoiceTwoId),
            mockTimelineFactory(),
            mockActiveViewLiveDataFactory(),
            mockChoiceTwo,
        )
        subject.observeForever {}

        assertEquals(mockChoiceTwo.value, subject.value)
    }

    @Test
    fun testSelectedIsTimelineItemWhenIdsMatch() {
        val mockTimelineId = "matches"
        val mockTimelineChoice = mockActiveViewLiveDataFactory(mockTimelineId)

        val subject = SelectedLiveData(
            MutableLiveData(mockTimelineId),
            listOf(
                mockTimelineChoice,
                mockActiveViewLiveDataFactory()
            ),
            mockActiveViewLiveDataFactory(),
            mockActiveViewLiveDataFactory(),
        )
        subject.observeForever {}

        assertEquals(mockTimelineChoice.value, subject.value)
    }

    @Test
    fun testSelectedUpdatesItemWhenIdsMatch() {
        val mockTimelineId = "matches"
        val mockTimelineChoice = mockActiveViewLiveDataFactory(mockTimelineId)
        val mockChoiceOne = mockActiveViewLiveDataFactory("mockChoiceOneId")
        val mockSelected = MutableLiveData(mockTimelineId)

        val subject = SelectedLiveData(
            mockSelected,
            listOf(
                mockTimelineChoice,
                mockActiveViewLiveDataFactory()
            ),
            mockChoiceOne,
            mockActiveViewLiveDataFactory(),
        )
        subject.observeForever {}

        mockSelected.value = mockChoiceOne.value!!.viewData.id

        assertEquals(mockChoiceOne.value, subject.value)
    }
}
