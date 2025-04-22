package com.example.data.remote.dto

import com.example.domain.model.Product
import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("data")
    val data: List<Product>
)