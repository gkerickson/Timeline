package com.erickson.timeline.smithsonian.requestdefinitions

object RequestDefinitions {

    interface ResponseType

    data class Body<T: ResponseType>(
        val status: Int,
        val responseCode: Int,
        val response: Response<T>
    ){
        data class Response<T: ResponseType>(
            val rows: List<T>
        )
    }

    data class SearchData(
        val id: String,
        val title: String,
        val content: ContentBody
    ): ResponseType {
        data class ContentBody(val descriptiveNonRepeating: Record) {
            data class Record(
                val id: String,
                val online_media: OnlineMediaBody
            ){
                data class OnlineMediaBody(val media: List<Media>) {
                    data class Media(
                        val guid: String,
                        val resources: List<Resource>
                    ){
                        data class Resource(
                            val label: String,
                            val url: String
                        )
                    }
                }
            }
        }
    }

    enum class ImageType(val type: String) {
        TIFF("High-resolution TIFF"),
        JPEG("High-resolution JPEG (3000x3000)"),
        SCREEN("Screen Image"),
        THUMBNAIL("Thumbnail Image")
    }

    interface Art {
        val id: String
    }

    enum class Category(val category: String) {
        CULTURE("culture"),
        DATA_SOURCE("data_source"),
        DATE("date"),
        OBJECT_TYPE("object_type"),
        ONLINE_MEDIA_TYPE("online_media_type"),
        PLACE("place"),
        TOPIC("topic"),
        UNIT_CODE("unit_code"),
    }

    enum class Terms(val term: String) {
        IMAGES("Images")
    }
}