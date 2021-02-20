package com.erickson.timeline

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody.Media
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    val smith: Smithsonian by lazy {
        Retrofit.Builder()
            .baseUrl(Smithsonian.url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Smithsonian::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mainImage = this.findViewById<ImageView>(R.id.selection_one)

        smith.getIds(Smithsonian.apiKey, Smithsonian.query)
            .enqueue(object : Callback<RequestDefinitions.Body<RequestDefinitions.SearchData>> {
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
                    response.body()?.run {
                        this.response.rows[0].content.descriptiveNonRepeating.online_media.media[0].resources
                            .find { resource: Media.Resource ->
                                resource.label == RequestDefinitions.ImageType.THUMBNAIL.type
                            }?.url
                    }?.let {
                        Picasso.get().load(it).into(mainImage)
                    }
                }
            })
    }
}