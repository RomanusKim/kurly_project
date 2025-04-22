package com.example.data.remote.dto

import com.example.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String,
    @SerializedName("originalPrice") val originalPrice: Int,
    @SerializedName("discountedPrice") val discountedPrice: Int?,
    @SerializedName("isSoldOut") val isSoldOut: Boolean
) {
    fun toDomain(): Product = Product(
        id = id,
        name = name,
        image = image,
        originalPrice = originalPrice,
        discountedPrice = discountedPrice,
        isSoldOut = isSoldOut
    )
}
