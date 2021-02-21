package com.erickson.timeline

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.*

class DataViewModel: ViewModel() {
    companion object {
        fun parseDate(date: String): Date  {
            var toParse = date
            if(date[date.length - 1] == 's') toParse = date.substring(0, date.length-1)
            return SimpleDateFormat("yyyy").parse(toParse)
        }

        fun processSearchDataResponseBody(body: RequestDefinitions.Body<RequestDefinitions.SearchData>): Map<String, ViewData> {
            return body.response.rows.mapNotNull { response ->
                response.content.run {
                    val date = parseDate(this.indexedStructured.date[0])
                    val notes: MutableList<Note> = mutableListOf()
                    freeText.name?.let { notes.addAll(it) }
                    freeText.notes?.let { notes.addAll(it) }

                    descriptiveNonRepeating.online_media.media.mapNotNull { media ->
                        media.resources?.find { resource ->
                            resource.label == RequestDefinitions.ImageType.THUMBNAIL.type
                        }?.url?.run {
                            ViewData(response.id, this, date, notes)
                        }
                    }.run {
                        if (isEmpty()) null
                        else this[0]
                    }
                }
            }.associateBy { it.id }
        }
    }

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
        val imageUrl: String,
        val date: Date,
        val notes: List<Note>,
    )

    var lastViewDataPress: Int = -1

    val allViewData = object : LiveData<Map<String, ViewData>>() {
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
                            response.body()?.let {
                                value = processSearchDataResponseBody(it)
                            }
                        }
                    })
            }
            super.onActive()
        }
    }

    data class ActiveViewData(
        val viewData: ViewData,
        val imageTarget: Target
    )

    class ImageTarget(private val callback: NotifyObserversCallback): Target {
        private var image: Bitmap? = null

        interface NotifyObserversCallback {
            fun notifyObservers()
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            callback.notifyObservers()
            image = bitmap
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            TODO("Not yet implemented")
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            TODO("Not yet implemented")
        }
    }

    val activeViewData = object: MediatorLiveData<List<ActiveViewData>>() {
        inner class Callback: ImageTarget.NotifyObserversCallback {
            override fun notifyObservers() {notifyObservers()}
        }

        override fun onActive() {
            if(this.value == null) {
                addSource(allViewData) { map ->
                    map.values.toList().subList(0,4).sortedBy { viewData ->
                        viewData.date
                    }.map { viewData ->
                        ActiveViewData(viewData, ImageTarget(Callback()).also {
                            Picasso.get().load(viewData.imageUrl).into(it)
                        })
                    }
                }
            }
        }
    }
}