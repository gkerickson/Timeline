package com.erickson.timeline.model

import com.erickson.timeline.smithsonian.RequestConstants.apiKey
import com.erickson.timeline.smithsonian.RequestConstants.query
import com.erickson.timeline.smithsonian.RequestConstants.url
import com.erickson.timeline.smithsonian.Smithsonian
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions
import com.erickson.timeline.smithsonian.requestdefinitions.RequestDefinitions.SearchData.ContentBody.Record.OnlineMediaBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.Assert.assertNotNull
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Sanity test to make sure Smithsonian apis are still behaving as expected and integrating with
 * retrofit well
 */
class ApiDefinitionTest {
    val client: OkHttpClient by lazy {
        OkHttpClient()
    }

    val smith: Smithsonian by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Smithsonian::class.java)
    }

    @Test
    fun testGetAnImageUrl() {
        val imageUrl: String =
            smith.getIds(apiKey, query).execute().body()?.run {
                this.response.rows.get(0).content.descriptiveNonRepeating.online_media.media.get(0).guid
            } ?: ""

        assert(imageUrl.isNotEmpty())
    }

    @Test
    fun testCanGetAnActualImage() {
        val media: OnlineMediaBody.Media? = smith.getIds(apiKey, query).execute().body()?.run {
            this.response.rows[0].content.descriptiveNonRepeating.online_media.media[0]
        }

        val imageUrl: String = media?.resources?.find {resource: OnlineMediaBody.Media.Resource ->
                resource.label == RequestDefinitions.ImageType.THUMBNAIL.type
        }?.url ?: ""

        val imageRequest = Request.Builder()
            .url(imageUrl)
            .build()

        val response = client.newCall(imageRequest).execute()

        assert(imageUrl.isNotEmpty())
        assertNotNull(response)
    }
}