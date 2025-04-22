package com.example.domain.repository

import com.example.domain.model.Product

interface ProductsRepository {
    suspend fun getProducts(url: String): List<Product>
}