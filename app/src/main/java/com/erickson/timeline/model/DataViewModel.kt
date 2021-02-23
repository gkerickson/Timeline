package com.erickson.timeline.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.model.livedata.ActiveViewLiveData
import com.erickson.timeline.model.livedata.HighestTimeLiveData
import com.erickson.timeline.model.livedata.LowestTimeLiveData
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DataViewModel @Inject constructor(
    val lowestTime: LowestTimeLiveData,
    val highestTime: HighestTimeLiveData,
    override val timelineViewData: List<ActiveViewLiveData>
) : ViewModel(), TimelineDataModel {

    private fun getMoreData() {
        RequestHandlerImpl.getData(object : RequestHandlerImpl.DataRequestCallback() {
            override fun withData(data: Map<String, ViewData>) {
                allViewData.putAll(data)

                listOf(
                    allViewData[nextActiveLiveDataIdFactory()],
                    allViewData[nextActiveLiveDataIdFactory()],
                    allViewData[nextActiveLiveDataIdFactory()],
                    allViewData[nextActiveLiveDataIdFactory()]
                ).sortedByDescending {
                    it?.date
                }.apply {
                    for (i in 0 until 4) {
                        this[i]?.let { timelineViewData[i].setValue(it) }
                    }
                }

                allViewData[nextActiveLiveDataIdFactory()]?.let { viewData ->
                    choiceOneActiveViewData.setValue(viewData)
                }
                allViewData[nextActiveLiveDataIdFactory()]?.let { viewData ->
                    choiceTwoActiveViewData.setValue(viewData)
                }
            }
        })
    }

    private var allViewData: MutableMap<String, ViewData> = mutableMapOf<String, ViewData>().also {
        getMoreData()
    }

    private fun nextActiveLiveDataIdFactory(): String? {
        allViewData.entries.mapNotNull {
            if (!usedIds.contains(it.key)) {
                it.key
            } else null
        }.let { freshKeys ->
            if (freshKeys.size < 10) {
                RequestHandlerImpl.getData(object : RequestHandlerImpl.DataRequestCallback() {
                    override fun withData(data: Map<String, ViewData>) {
                        allViewData.putAll(data)
                    }
                })
            }
            freshKeys.getOrNull(0)?.let {
                usedIds.add(it)
                return it
            }
        }
        return null
    }

    fun updateChoices() {
        val newData = listOf(
            choiceOneActiveViewData,
            choiceTwoActiveViewData,
            timelineViewData[1],
            timelineViewData[2]
        ).sortedByDescending {
            it.value!!.viewData.date
        }.map {
            it.value?.viewData
        }
        for (i in 0 until 4) {
            newData[i]?.let { timelineViewData[i].setValue(it) }
        }
        allViewData[nextActiveLiveDataIdFactory()]?.let {
            choiceOneActiveViewData.setValue(it)
        }
        allViewData[nextActiveLiveDataIdFactory()]?.let {
            choiceTwoActiveViewData.setValue(it)
        }
    }

    private val usedIds = mutableListOf<String>()
    private val choiceOneActiveViewData = ActiveViewLiveData(RequestHandlerImpl)
    private val choiceTwoActiveViewData = ActiveViewLiveData(RequestHandlerImpl)

    val selectedIsHigher: Boolean
        get() {
            fun isSelected(check: ActiveViewLiveData): Boolean =
                check.value?.viewData?.id?.let {
                    it.isNotEmpty() && it == selected.value?.viewData?.id
                } ?: false

            return (isSelected(choiceOneActiveViewData) && choiceOneActiveViewData > choiceTwoActiveViewData) ||
                    (isSelected(choiceTwoActiveViewData) && choiceOneActiveViewData < choiceTwoActiveViewData)
        }
    val shouldShowButton: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            fun update() {
                value = selected.value?.viewData?.id?.run {
                    (this == choiceOneActiveViewData.value?.viewData?.id ||
                            this == choiceTwoActiveViewData.value?.viewData?.id)
                } ?: false
            }
            addSource(selected) { update() }
            addSource(choiceOneActiveViewData) { update() }
            addSource(choiceTwoActiveViewData) { update() }
        }
    }
    override val selectedId: MutableLiveData<String> = MutableLiveData("")

    override val choiceOneViewData: LiveData<ActiveViewData> = choiceOneActiveViewData
    override val choiceTwoViewData: LiveData<ActiveViewData> = choiceTwoActiveViewData
    override val selected: MediatorLiveData<ActiveViewData> by lazy {
        MediatorLiveData<ActiveViewData>().apply {
            addSource(selectedId) { id ->
                timelineViewData.forEach { timelineLiveData ->
                    if (timelineLiveData.value?.viewData?.id == id)
                        value = timelineLiveData.value
                }

                if (choiceOneViewData.value?.viewData?.id == id)
                    value = choiceOneViewData.value

                if (choiceTwoViewData.value?.viewData?.id == id)
                    value = choiceTwoActiveViewData.value
            }
        }
    }
}
