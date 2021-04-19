package com.sachin.sasthainsta

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tags(
    @Json(name = "data")
    val tagsData: TagData?,

    @Json(name = "status")
    val status: Int,

    @Json(name = "success")
    val success: Boolean,
)

@JsonClass(generateAdapter = true)
data class TagData(
    @Json(name = "items")
    val postList: List<Post>?,
)

@JsonClass(generateAdapter = true)
data class Feed(
    @Json(name = "data")
    val postListList: List<Post>?,

    @Json(name = "status")
    val status: Int,

    @Json(name = "success")
    val success: Boolean,
)

data class Post(
    @Json(name = "id")
    val id: String,

    @Json(name = "title")
    val title: String?,

    @Json(name = "description")
    val description: String?,

    @Json(name = "link")
    val link: String,

    @Json(name = "type")
    val type: String?,

    @Json(name = "is_album")
    val isAlbum: Boolean?,

    @Json(name = "images")
    val mediaList: List<Media>?,
)

data class Media(
    @Json(name = "id")
    val id: String,

    @Json(name = "title")
    val title: String?,

    @Json(name = "description")
    val description: String?,

    @Json(name = "link")
    val link: String,

    @Json(name = "type")
    val type: String?,
)
