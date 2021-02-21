package com.erickson.timeline.smithsonian

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import com.erickson.timeline.model.DataViewModel
import com.erickson.timeline.smithsonian.RequestConstants.apiKey
import com.erickson.timeline.smithsonian.RequestConstants.query
import com.erickson.timeline.smithsonian.RequestConstants.url
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object RequestHandlerImpl: RequestHandler {
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    private val smith: Smithsonian by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Smithsonian::class.java)
    }

    @SuppressLint("NewApi", "SimpleDateFormat")
    fun parseDate(date: String): Date {
        var toParse = date
        if (date[date.length - 1] == 's') toParse = date.substring(0, date.length - 1)
        return SimpleDateFormat("yyyy").parse(toParse)
    }

    fun processSearchDataResponseBody(body: RequestDefinitions.Body<RequestDefinitions.SearchData>): Map<String, DataViewModel.ViewData> {
        return body.response.rows.mapNotNull { response ->
            response.content.run {
                val date = this.indexedStructured.date.getOrNull(0)?.let { parseDate(it) }
                val notes: MutableList<RequestDefinitions.SearchData.ContentBody.FreeText.Note> =
                    mutableListOf()
                freeText.name?.let { notes.addAll(it) }
                freeText.notes?.let { notes.addAll(it) }

                descriptiveNonRepeating.online_media.media.mapNotNull { media ->
                    media.resources?.find { resource ->
                        resource.label == RequestDefinitions.ImageType.THUMBNAIL.type
                    }?.url?.run {
                        DataViewModel.ViewData(response.id, this, date ?: Date(), notes)
                    }
                }.run {
                    if (isEmpty()) null
                    else this[0]
                }
            }
        }.associateBy { it.id }
    }

    abstract class DataRequestCallback {
        val retrofitCallbackHandler = object: Callback<RequestDefinitions.Body<RequestDefinitions.SearchData>> {
            override fun onFailure(
                call: Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>,
                t: Throwable
            ) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<RequestDefinitions.Body<RequestDefinitions.SearchData>>,
                response: Response<RequestDefinitions.Body<RequestDefinitions.SearchData>>
            ) {
                response.body()?.let {
                    withData(processSearchDataResponseBody(it))
                }
            }
        }
        abstract fun withData(data: Map<String, DataViewModel.ViewData>)
    }

    override fun getData(callback: DataRequestCallback) {
        smith.getIds(apiKey, query).enqueue(callback.retrofitCallbackHandler)
    }
}