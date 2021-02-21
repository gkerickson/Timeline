package com.erickson.timeline.model

import androidx.lifecycle.LiveData
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl

class SmithsonianLiveData(private val handler: RequestHandler) :
    LiveData<Map<String, ViewData>>() {

    override fun onActive() {
        if (this.value == null) {
            handler.getData(object : RequestHandlerImpl.DataRequestCallback() {
                override fun withData(data: Map<String, ViewData>) {
                    value = data
                }
            })
        }
        super.onActive()
    }
}