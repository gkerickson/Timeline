package com.erickson.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DataViewModel: ViewModel() {
    private val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    private val smith: Smithsonian by lazy {
        Retrofit.Builder()
            .baseUrl(Smithsonian.url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Smithsonian::class.java)
    }

    data class ViewData(
        val id: String,
        val imageUrl: String
    )

    val potentialImages = object : LiveData<List<ViewData>>() {
        override fun onActive() {
            if (this.value == null) {
                smith.getIds(Smithsonian.apiKey, Smithsonian.query)
                    .enqueue(object :
                        Callback<RequestDefinitions.Body<RequestDefinitions.SearchData>> {
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
                            value = response.body()?.run {
                                this.response.rows.mapNotNull { response ->
                                    response.content.descriptiveNonRepeating.online_media.media.mapNotNull { media ->
                                        media.resources?.find { resource ->
                                            resource.label == RequestDefinitions.ImageType.THUMBNAIL.type
                                        }?.url?.run {
                                            ViewData(response.id, this)
                                        }
                                    }.run {
                                        if (isEmpty()) null
                                        else this[0]
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}