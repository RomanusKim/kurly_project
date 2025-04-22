package com.example.data.datasource

import com.example.data.remote.api.SectionApiService
import com.example.data.remote.dto.SectionResponse
import javax.inject.Inject

class SectionRemoteDataSource @Inject constructor(
    private val sectionApiService: SectionApiService
) {
    suspend fun getSections(page: Int): SectionResponse = sectionApiService.getSections(page)
}