package com.example.domain.usecase

import com.example.domain.model.Section
import com.example.domain.repository.SectionRepository
import javax.inject.Inject

class GetSectionsUseCase @Inject constructor(
    private val repository: SectionRepository
) {
    suspend operator fun invoke(page: Int): Pair<List<Section>, Int?> {
        return repository.getSections(page)
    }
}