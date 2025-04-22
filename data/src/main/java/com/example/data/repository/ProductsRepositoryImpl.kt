package com.example.data.repository

import com.example.data.datasource.ProductRemoteDataSource
import com.example.domain.model.Product
import com.example.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) : ProductsRepository{
    override suspend fun getProducts(url: String): List<Product> {
        return productRemoteDataSource.getProducts(url).data
    }
}