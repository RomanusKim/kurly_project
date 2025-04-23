package com.example.kurly_project.mapper

import com.example.domain.model.Section
import com.example.kurly_project.model.ProductUiModel

data class SectionWithProducts(
    val section: Section,
    val products: List<ProductUiModel>
)
