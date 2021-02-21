package com.erickson.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.TimelineDataModel
import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DetailsAdapterTest {
    val selectedViewData = ViewData(
        "selectedViewData",
        "URL",
        Date(),
        listOf(
            Note(
                "Label-1",
                "content-1"
            )
        )
    )

    private fun selectedViewDataFactory(notes: Int): ViewData {
        return ViewData(
            "selectedViewData",
            "URL",
            Date(),
            mutableListOf<Note>().also {
                for (i in 0 until notes) {
                    it.add(
                        Note(
                            "Label - $i",
                            "Conent - $i"
                        )
                    )
                }
            }
        )
    }

    val mockAllViewData = MutableLiveData<Map<String, ViewData>>(
        mutableMapOf<String, ViewData>().also { map ->
            listOf(
                selectedViewData,
                TestHelpers.mockViewDataFactory(),
                TestHelpers.mockViewDataFactory(),
                TestHelpers.mockViewDataFactory()
            ).forEach {
                map[it.id] = it
            }
        }
    )

    private fun mockAllViewDataFactory(toAdd: ViewData?): MutableLiveData<Map<String, ViewData>> {
        return MutableLiveData<Map<String, ViewData>>(
            mutableMapOf<String, ViewData>().also { map ->
                listOf(
                    selectedViewData,
                    toAdd ?: TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory(),
                    TestHelpers.mockViewDataFactory()
                ).forEach {
                    map[it.id] = it
                }
            }
        )
    }

    @Test
    fun testGetItemCountForOneNote() {
        val mockViewModel = object : TimelineDataModel {
            override val allViewData: LiveData<Map<String, ViewData>> = mockAllViewData
            override val timelineViewData: LiveData<List<ActiveViewData>> = MutableLiveData()
            override var selectedId: String = selectedViewData.id
        }

        val subject = DetailsAdapter(mockViewModel)

        assertEquals(1, subject.itemCount)
    }

    @Test
    fun testGetItemCountWithTenNotes() {
        val selectedViewData = selectedViewDataFactory(10)
        val mockAllViewData = mockAllViewDataFactory(selectedViewData)
        val mockViewModel = object : TimelineDataModel {
            override val allViewData: LiveData<Map<String, ViewData>> = mockAllViewData
            override val timelineViewData: LiveData<List<ActiveViewData>> = MutableLiveData()
            override var selectedId: String = selectedViewData.id
        }

        val subject = DetailsAdapter(mockViewModel)

        assertEquals(10, subject.itemCount)
    }

    @Test
    fun testOnBindViewHolderSetsUpViewHolder() {
        val selectedViewData = selectedViewDataFactory(1)
        val mockAllViewData = mockAllViewDataFactory(selectedViewData)
        val mockViewModel = object : TimelineDataModel {
            override val allViewData: LiveData<Map<String, ViewData>> = mockAllViewData
            override val timelineViewData: LiveData<List<ActiveViewData>> = MutableLiveData()
            override var selectedId: String = selectedViewData.id
        }

        val subject = DetailsAdapter(mockViewModel)

        val viewHolder: DetailViewFragment.NoteViewHolder = mock()
        whenever(viewHolder.labelView).thenReturn(mock())
        whenever(viewHolder.contentView).thenReturn(mock())

        subject.onBindViewHolder(viewHolder, 0)

        verify(viewHolder.labelView).text = selectedViewData.notes[0].label
        verify(viewHolder.contentView).text = selectedViewData.notes[0].content
    }

    @Test
    fun testOnBindViewHolderWithNoNotesDoesntThrowAnException() {
        val selectedViewData = selectedViewDataFactory(0)
        val mockAllViewData = mockAllViewDataFactory(selectedViewData)
        val mockViewModel = object : TimelineDataModel {
            override val allViewData: LiveData<Map<String, ViewData>> = mockAllViewData
            override val timelineViewData: LiveData<List<ActiveViewData>> = MutableLiveData()
            override var selectedId: String = selectedViewData.id
        }

        val subject = DetailsAdapter(mockViewModel).onBindViewHolder(mock(), 0)
    }
}