package com.erickson.timeline.smithsonian

import android.util.Log
import com.erickson.timeline.model.ViewData
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class DataRequestCallback {
    val retrofitCallbackHandler =
        object : Callback<RequestDefinitions.Body<RequestDefinitions.SearchData>> {
            override fun onFailure(
                call: Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>,
                t: Throwable
            ) {
                Log.e("RetrofitCallback", "FAILURE with ${t.stackTrace}")
            }

            override fun onResponse(
                call: Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>,
                response: Response<RequestDefinitions.Body<RequestDefinitions.SearchData>>
            ) {
                response.body()?.let {
                    withData(RequestHandlerImpl.processSearchDataResponseBody(it))
                }
            }
        }

    abstract fun withData(data: Map<String, ViewData>)
}
