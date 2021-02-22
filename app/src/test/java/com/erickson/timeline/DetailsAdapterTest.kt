package com.erickson.timeline

import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class DetailsAdapterTest {

    private fun mockViewDataFactory(notes: Int): ViewData {
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

    @Test
    fun testGetItemCountForOneNote() {
        val subject = DetailsAdapter(
            MutableLiveData(
                ActiveViewData(
                    mockViewDataFactory(1),
                    null
                )
            )
        )

        assertEquals(1, subject.itemCount)
    }

    @Test
    fun testGetItemCountWithTenNotes() {
        val selectedViewData = mockViewDataFactory(10)

        val subject = DetailsAdapter(
            MutableLiveData(
                ActiveViewData(
                    selectedViewData,
                    null
                )
            )
        )

        assertEquals(10, subject.itemCount)
    }

    @Test
    fun testOnBindViewHolderSetsUpViewHolder() {
        val selectedViewData = mockViewDataFactory(1)

        val subject = DetailsAdapter(
            MutableLiveData(
                ActiveViewData(
                    selectedViewData,
                    null
                )
            )
        )

        val viewHolder: DetailViewFragment.NoteViewHolder = mock()
        whenever(viewHolder.labelView).thenReturn(mock())
        whenever(viewHolder.contentView).thenReturn(mock())

        subject.onBindViewHolder(viewHolder, 0)

        verify(viewHolder.labelView).text = selectedViewData.notes[0].label
        verify(viewHolder.contentView).text = selectedViewData.notes[0].content
    }

    @Test
    fun testOnBindViewHolderWithNoNotesDoesntThrowAnException() {
        val selectedViewData = mockViewDataFactory(0)

        DetailsAdapter(
            MutableLiveData(
                ActiveViewData(
                    selectedViewData,
                    null
                )
            )
        ).onBindViewHolder(mock(), 0)
    }
}