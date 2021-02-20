package com.erickson.timeline

import com.erickson.timeline.Smithsonian.Companion.ImageType
import com.erickson.timeline.Smithsonian.Companion.SearchData.ContentBody.Record.OnlineMediaBody.Media
import com.squareup.picasso.Picasso
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
            .baseUrl(Smithsonian.url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(Smithsonian::class.java)
    }

    @Test
    fun testGetAnImageUrl() {
        val imageUrl: String =
            smith.getIds(Smithsonian.apiKey, Smithsonian.query).execute().body()?.run {
                this.response.rows.get(0).content.descriptiveNonRepeating.online_media.media.get(0).guid
            } ?: ""

        assert(imageUrl.isNotEmpty())
    }

    @Test
    fun testCanGetAnActualImage() {
        val media: Media? = smith.getIds(Smithsonian.apiKey, Smithsonian.query).execute().body()?.run {
            this.response.rows[0].content.descriptiveNonRepeating.online_media.media[0]
        }

        val imageUrl: String = media?.resources?.find {resource: Media.Resource ->
                resource.label == ImageType.THUMBNAIL.type
        }?.url ?: ""

        val imageRequest = Request.Builder()
            .url(imageUrl)
            .build()

        val response = client.newCall(imageRequest).execute()

        assert(imageUrl.isNotEmpty())
        assertNotNull(response)
    }
}