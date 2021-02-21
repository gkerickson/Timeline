package com.erickson.timeline.smithsonian

import com.erickson.timeline.smithsonian.RequestConstants.API_KEY
import com.erickson.timeline.smithsonian.RequestConstants.QUERY
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Smithsonian {
    @GET("category/art_design/search")
    fun getIds(
        @Query("api_key") key: String = API_KEY,
        @Query("q") query: String = QUERY,
        @Query("rows") rows: Int = 10,
        @Query("start") start: Int = 0
    ): Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>
}