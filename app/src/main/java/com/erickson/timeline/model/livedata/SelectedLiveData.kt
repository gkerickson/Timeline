package com.erickson.timeline.model.livedata

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.ActiveViewData
import com.erickson.timeline.model.ChoiceOneActiveViewLiveData
import com.erickson.timeline.model.ChoiceTwoActiveViewLiveData
import javax.inject.Inject

class SelectedLiveData @Inject constructor(
    selectedId: MutableLiveData<String>,
    timelineViewData: List<ActiveViewLiveData>,
    @ChoiceOneActiveViewLiveData choiceOneViewData: ActiveViewLiveData,
    @ChoiceTwoActiveViewLiveData choiceTwoViewData: ActiveViewLiveData,
) : MediatorLiveData<ActiveViewData>() {
    init {
        addSource(selectedId) { id ->
            timelineViewData.forEach { timelineLiveData ->
                if (timelineLiveData.value?.viewData?.id == id)
                    value = timelineLiveData.value
            }

            if (choiceOneViewData.value?.viewData?.id == id)
                value = choiceOneViewData.value

            if (choiceTwoViewData.value?.viewData?.id == id)
                value = choiceTwoViewData.value
        }
    }
}