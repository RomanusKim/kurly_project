package com.example.data.remote.dto

import com.example.domain.model.Section
import com.google.gson.annotations.SerializedName

data class SectionResponse(
    @SerializedName("data")
    val data: List<Section>,
    @SerializedName("paging")
    val paging: Paging?
)

data class Paging(
    @SerializedName("next_page")
    val nextPage: Int?
)