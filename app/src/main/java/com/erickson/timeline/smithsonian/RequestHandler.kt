package com.erickson.timeline.smithsonian

import com.squareup.picasso.Target

interface RequestHandler {
    fun getData(callback: DataRequestCallback)
    fun loadImage(url: String, target: Target)
}