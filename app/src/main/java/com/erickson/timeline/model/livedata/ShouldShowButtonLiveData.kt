package com.erickson.timeline.model.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.erickson.timeline.model.ActiveViewData

class ShouldShowButtonLiveData constructor(
    selected: LiveData<ActiveViewData>,
    choiceOneActiveViewData: LiveData<ActiveViewData>,
    choiceTwoActiveViewData: LiveData<ActiveViewData>
) : MediatorLiveData<Boolean>() {
    init {
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