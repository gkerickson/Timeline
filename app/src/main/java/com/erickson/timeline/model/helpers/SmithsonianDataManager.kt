package com.erickson.timeline.model.helpers

import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.DataRequestCallback
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl

class SmithsonianDataManager(
    private val setupCallback: SetupCallback,
    private val requestHandler: RequestHandler
) {
    interface SetupCallback {
        fun onSetupComplete()
    }

    fun getNextViewData(): ViewData {
        return allViewData[nextActiveLiveDataIdFactory()]!!
    }

    private var allViewData: MutableMap<String, ViewData> = mutableMapOf<String, ViewData>().also {
        requestHandler.getData(object : DataRequestCallback() {
            override fun withData(data: Map<String, ViewData>) {
                it.putAll(data)
                setupCallback.onSetupComplete()
            }
        })
    }

    private val usedIds = mutableListOf<String>()

    private fun nextActiveLiveDataIdFactory(): String? {
        allViewData.entries.mapNotNull {
            if (!usedIds.contains(it.key)) {
                it.key
            } else null
        }.let { freshKeys ->
            if (freshKeys.size < 10) {
                requestHandler.getData(object : DataRequestCallback() {
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
}