package com.wla.api

import com.wla.models.ObjectId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("public/collection/v1/search")
    suspend fun searchObjectIds(@Query("hasImages") hasImages: Boolean = true, @Query("q") q: String): Response<ObjectId>
}