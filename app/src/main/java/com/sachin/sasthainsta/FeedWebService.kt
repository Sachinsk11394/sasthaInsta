package com.sachin.sasthainsta

import retrofit2.Response
import retrofit2.http.GET

interface FeedWebService {
    /**
     * Get all categories from server
     */
    @GET("gallery/t/dogs_in_sweaters")
    suspend fun tags(): Response<Tags>

    /**
     * Get top media
     */
    @GET("gallery/top")
    suspend fun galleryTop(): Response<Feed>

    /**
     * Get hot media
     */
    @GET("gallery/hot")
    suspend fun galleryHot(): Response<Feed>
}