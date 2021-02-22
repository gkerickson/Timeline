package com.erickson.timeline.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import java.util.*

class DataViewModel : ViewModel(), TimelineDataModel {
    private var allViewData: MutableMap<String, ViewData> = run {
        RequestHandlerImpl.getData(object : RequestHandlerImpl.DataRequestCallback() {
            override fun withData(data: Map<String, ViewData>) {
                allViewData.putAll(data)
                timelineViewData.forEach { liveData ->
                    allViewData[nextActiveLiveDataIdFactory()]?.let { viewData ->
                        Log.e("GALEN", "SETTING")
                        liveData.setValue(viewData)
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
        mutableMapOf()
    }

    private fun nextActiveLiveDataIdFactory(): String? {
        allViewData.entries.forEach {
            if (!usedIds.contains(it.key)) {
                usedIds.add(it.key)
                return it.key
            }
        }
        return null
    }

    fun updateChoices() {
        val newData = listOf(choiceOneActiveViewData, choiceTwoActiveViewData, timelineViewData[1], timelineViewData[2]).sortedBy {
            it.value!!.viewData.date
        }.map{
            it.value?.viewData
        }
        for(i in 0 until 4) {
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

    val lowestTime by lazy {
        MediatorLiveData<Date>().apply {
            timelineViewData.forEach { timelineLiveData ->
                addSource(timelineLiveData) {
                    it.viewData.date.let { newDate ->
                        this.value?.let { oldDate ->
                            if (oldDate > newDate) this.value = newDate
                        } ?: run {
                            this.value = newDate
                        }
                    }
                }
            }
        }
    }
    val highestTime by lazy {
        MediatorLiveData<Date>().apply {
            timelineViewData.forEach { timelineLiveData ->
                addSource(timelineLiveData) {
                    it.viewData.date.let { newDate ->
                        this.value?.let { oldDate ->
                            if (oldDate < newDate) this.value = newDate
                        } ?: run {
                            this.value = newDate
                        }
                    }
                }
            }
        }
    }
    val shouldShowButton: MediatorLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(selected) { selected ->
                value = selected.viewData.id.run {
                    (this == choiceOneActiveViewData.value?.viewData?.id ||
                            this == choiceTwoActiveViewData.value?.viewData?.id)
                }
            }
        }
    }
    override val selectedId: MutableLiveData<String> = MutableLiveData("")
    override val timelineViewData: List<ActiveViewLiveData> = listOf(
        ActiveViewLiveData(RequestHandlerImpl),
        ActiveViewLiveData(RequestHandlerImpl),
        ActiveViewLiveData(RequestHandlerImpl),
        ActiveViewLiveData(RequestHandlerImpl),
    )
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