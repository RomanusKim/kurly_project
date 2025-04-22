package com.example.data.datasource

import com.example.data.remote.api.ProductApiService
import com.example.data.remote.dto.ProductResponse
import javax.inject.Inject

class ProductRemoteDataSource @Inject constructor(
    private val productApiService: ProductApiService
) {

    suspend fun getProducts(url: String): ProductResponse = productApiService.getProducts(url)
}