package com.example.data.remote.api

import com.example.data.remote.dto.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {
    @GET("section/products")
    suspend fun getProducts(
        @Query("url") url: String
    ): ProductResponse

}