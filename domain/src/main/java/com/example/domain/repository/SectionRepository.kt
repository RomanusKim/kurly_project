package com.example.domain.repository

import com.example.domain.model.Section

interface SectionRepository {
    suspend fun getSections(page: Int): Pair<List<Section>, Int?>
}