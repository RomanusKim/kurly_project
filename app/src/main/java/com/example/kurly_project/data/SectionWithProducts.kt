package com.example.kurly_project.data

import com.example.domain.model.Product
import com.example.domain.model.Section

data class SectionWithProducts(
    val section: Section,
    val products: List<Product>
)
