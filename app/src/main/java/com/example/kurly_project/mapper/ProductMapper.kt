package com.example.kurly_project.mapper

import com.example.domain.model.Product
import com.example.kurly_project.model.ProductUiModel


fun Product.toUiModel(): ProductUiModel {
    return ProductUiModel(
        id = id,
        name = name,
        image = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isWished = false
    )
}