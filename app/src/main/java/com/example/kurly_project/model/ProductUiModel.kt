package com.example.kurly_project.model

data class ProductUiModel(
    val id: Long,
    val name: String,
    val image: String,
    val originalPrice: Int,
    val discountedPrice: Int?,
    var isWished: Boolean = false // 로컬 전용 필드
)
