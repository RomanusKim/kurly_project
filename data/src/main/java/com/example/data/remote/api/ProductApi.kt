package com.example.data.remote.api

import com.example.data.remote.dto.ProductResponse
import retrofit2.http.GET

interface ProductApi {
    @GET("section/products")
    suspend fun getProducts(): List<ProductResponse>

}