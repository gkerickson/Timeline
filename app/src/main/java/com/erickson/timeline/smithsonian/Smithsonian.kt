package com.erickson.timeline.smithsonian

import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Smithsonian {
    companion object {
        val apiKey = "LF2INfyYBeUsNx5cm6gYSVMLMh7R11trAhX0AAwP"
        val url = "https://api.si.edu/openaccess/api/v1.0/"
        val query = "SELECT id WHERE online_media_type is Images"
    }
//    Request URL: https://api.si.edu/openaccess/api/v1.0/category/art_design/search?q=SELECT+id+WHERE+online_media_type+is+Images&api_key=LF2INfyYBeUsNx5cm6gYSVMLMh7R11trAhX0AAwP

//    @GET("stats")
//    fun getStats(@Query("api_key") key: String): Call<Body<Any>>

    //    @GET("category/art_design/search?q=SELECT+id+WHERE+online_media_type+is+Images")
    @GET("category/art_design/search")
    fun getIds(@Query("api_key") key: String, @Query("q") query: String): Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>
}