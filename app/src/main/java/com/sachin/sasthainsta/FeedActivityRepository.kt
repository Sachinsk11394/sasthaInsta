package com.sachin.sasthainsta

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class FeedActivityRepository(private val webService: FeedWebService,
                                  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {
    suspend fun tags(): NetworkResponse<Tags> {
        return withContext(ioDispatcher){
            val response = webService.tags()
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<Tags>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }

    suspend fun galleryTop(): NetworkResponse<Feed> {
        return withContext(ioDispatcher){
            val response = webService.galleryTop()
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<Feed>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }

    suspend fun galleryHot(): NetworkResponse<Feed> {
        return withContext(ioDispatcher){
            val response = webService.galleryHot()
            if (response.isSuccessful) {
                return@withContext NetworkResponse.success(response.body()!!)
            } else {
                return@withContext NetworkResponse.failure<Feed>(Throwable("Something went wrong, Please try again later."))
            }
        }
    }
}