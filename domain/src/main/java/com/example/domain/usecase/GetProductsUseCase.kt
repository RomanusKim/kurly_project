package com.example.domain.usecase

import com.example.domain.model.Product
import com.example.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(url: String): List<Product> = repository.getProducts(url)
}