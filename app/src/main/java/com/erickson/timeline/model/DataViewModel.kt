package com.erickson.timeline.model

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.erickson.timeline.smithsonian.RequestConstants.url
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.FreeText.Note
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class DataViewModel : ViewModel() {

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

    data class ViewData(
        val id: String,
        val imageUrl: String,
        val date: Date,
        val notes: List<Note>,
    )

    data class ActiveViewData(
        val viewData: ViewData,
        val imageTarget: ImageTarget
    )

    val allViewData = SmithsonianLiveData(RequestHandlerImpl)
    val timelineViewData = ActiveViewLiveData(allViewData)
    var selectedId: String = ""

    class ImageTarget(private val callback: NotifyObserversCallback) : Target {
        var image: Bitmap? = null
            private set

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

}