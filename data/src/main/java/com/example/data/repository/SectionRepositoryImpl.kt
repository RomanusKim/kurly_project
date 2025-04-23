package com.example.data.repository

import com.example.data.datasource.SectionRemoteDataSource
import com.example.data.remote.dto.SectionResponse
import com.example.domain.model.Section
import com.example.domain.repository.SectionRepository
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val sectionRemoteDataSource: SectionRemoteDataSource
) : SectionRepository {
    override suspend fun getSections(page: Int): Pair<List<Section>, Int?> {
        val response = sectionRemoteDataSource.getSections(page)
        return Pair(response.data, response.paging?.nextPage)
    }
}