package com.erickson.timeline.smithsonian

interface RequestHandler {
    fun getData(callback: RequestHandlerImpl.DataRequestCallback)
}