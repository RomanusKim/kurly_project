package com.example.data.remote.api

import com.example.data.remote.dto.SectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SectionApi {
    @GET("sections")
    suspend fun getSections(@Query("page") page: Int): List<SectionResponse>
}