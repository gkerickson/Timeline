package com.erickson.timeline.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.model.helpers.SmithsonianDataManager
import com.erickson.timeline.model.livedata.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    val selected: SelectedLiveData,
    val lowestTime: LowestTimeLiveData,
    val highestTime: HighestTimeLiveData,
    val timelineViewData: List<ActiveViewLiveData>,
    val selectedId: MutableLiveData<String>,
    @ChoiceOneActiveViewLiveData private val choiceOneActiveViewData: ActiveViewLiveData,
    @ChoiceTwoActiveViewLiveData private val choiceTwoActiveViewData: ActiveViewLiveData,
    private val manager: SmithsonianDataManager
) : ViewModel() {
    init {
        manager.setupDataManagerReadyCallback(object : SmithsonianDataManager.SetupCallback {
            override fun onReady() {
                setupViewModel()
            }
        })
    }

    val choiceOneViewData: LiveData<ActiveViewData> = choiceOneActiveViewData
    val choiceTwoViewData: LiveData<ActiveViewData> = choiceTwoActiveViewData
    val shouldShowButton: LiveData<Boolean> =
        ShouldShowButtonLiveData(selected, choiceOneViewData, choiceTwoViewData)

    private fun updateViewData(timelineList: List<ViewData>) {
        timelineList.sortedByDescending {
            it.date
        }.apply {
            for (i in 0 until 4) {
                this[i].let { timelineViewData[i].setValue(it) }
            }
        }
        manager.getNextViewData().let { viewData ->
            choiceOneActiveViewData.setValue(viewData)
        }
        manager.getNextViewData().let { viewData ->
            choiceTwoActiveViewData.setValue(viewData)
        }
    }

    private fun setupViewModel() {
        updateViewData(
            listOf(
                manager.getNextViewData(),
                manager.getNextViewData(),
                manager.getNextViewData(),
                manager.getNextViewData()
            )
        )
    }

    fun updateChoices() {
        updateViewData(
            listOf(
                choiceOneActiveViewData.value?.viewData,
                choiceTwoActiveViewData.value?.viewData,
                timelineViewData[1].value?.viewData,
                timelineViewData[2].value?.viewData
            ).map { it ?: manager.getNextViewData() }
        )
    }

    val selectedIsHigher: Boolean
        get() {
            fun isSelected(check: ActiveViewLiveData): Boolean =
                check.value?.viewData?.id?.let {
                    it.isNotEmpty() && it == selected.value?.viewData?.id
                } ?: false

            return (isSelected(choiceOneActiveViewData) && choiceOneActiveViewData > choiceTwoActiveViewData) ||
                    (isSelected(choiceTwoActiveViewData) && choiceOneActiveViewData < choiceTwoActiveViewData)
        }
}
