package com.example.data.repository

import com.example.data.datasource.SectionRemoteDataSource
import com.example.domain.model.Section
import com.example.domain.repository.SectionRepository
import javax.inject.Inject

class SectionRepositoryImpl @Inject constructor(
    private val sectionRemoteDataSource: SectionRemoteDataSource
) : SectionRepository {
    override suspend fun getSections(page: Int): List<Section> {
        return sectionRemoteDataSource.getSections(page).data
    }
}