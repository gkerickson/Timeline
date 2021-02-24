package com.erickson.timeline.model.livedata

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.TestHelpers.mockActiveViewDataFactory
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.ViewData
import org.junit.Test
import java.util.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule

class ShouldShowButtonLiveDataTest {
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private lateinit var mockChoiceOne: MutableLiveData<ActiveViewData>
    private lateinit var mockChoiceTwo: MutableLiveData<ActiveViewData>
    private lateinit var mockOther: MutableLiveData<ActiveViewData>

    @Before
    fun setup() {
        mockOther = MutableLiveData(mockActiveViewDataFactory("mockOther"))
        mockChoiceOne = MutableLiveData(mockActiveViewDataFactory("mockOneId"))
        mockChoiceTwo = MutableLiveData(mockActiveViewDataFactory("mockTwoId"))
    }

    @Test
    fun shouldShowButtonIfSelectedIsChoiceOne() {
        val mockSelected = MutableLiveData(mockChoiceOne.value!!)

        val subject = ShouldShowButtonLiveData(mockSelected, mockChoiceOne, mockChoiceTwo)
        subject.observeForever{}

        assertEquals(true, subject.value)
    }

    @Test
    fun shouldShowButtonIfSelectedIsChoiceTwo() {
        val mockSelected = MutableLiveData(mockChoiceTwo.value!!)

        val subject = ShouldShowButtonLiveData(mockSelected, mockChoiceOne, mockChoiceTwo)
        subject.observeForever{}

        assertEquals(true, subject.value)
    }

    @Test
    fun shouldNotShowButtonIfSelectedIsNeither() {
        val subject = ShouldShowButtonLiveData(mockOther, mockChoiceOne, mockChoiceTwo)
        subject.observeForever{}

        assertEquals(false, subject.value)
    }

    @Test
    fun shouldShowButtonIfSelectedChangesToChoiceOne() {
        val subject = ShouldShowButtonLiveData(mockOther, mockChoiceOne, mockChoiceTwo)
        mockOther.value = mockChoiceOne.value
        subject.observeForever{}

        assertEquals(true, subject.value)
    }

    @Test
    fun shouldShowButtonIfSelectedChangesToChoiceTwo() {
        val subject = ShouldShowButtonLiveData(mockOther, mockChoiceOne, mockChoiceTwo)
        mockOther.value = mockChoiceTwo.value
        subject.observeForever{}

        assertEquals(true, subject.value)
    }

    @Test
    fun shouldNotShowButtonIfSelectedChangesToNeither() {
        val oldOther = mockOther.value
        mockOther.value = mockChoiceOne.value

        val subject = ShouldShowButtonLiveData(mockOther, mockChoiceOne, mockChoiceTwo)
        subject.observeForever{}
        mockOther.value = oldOther

        assertEquals(false, subject.value)
    }
}