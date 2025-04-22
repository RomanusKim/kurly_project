package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SectionResponse(
    @SerializedName("title") val title: String,
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String, // "horizontal", "vertical", "grid"
    @SerializedName("url") val url: String
)
