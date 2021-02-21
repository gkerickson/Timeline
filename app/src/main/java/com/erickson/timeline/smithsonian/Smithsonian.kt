package com.erickson.timeline.smithsonian

import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Smithsonian {
    @GET("category/art_design/search")
    fun getIds(@Query("api_key") key: String, @Query("q") query: String): Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>
}