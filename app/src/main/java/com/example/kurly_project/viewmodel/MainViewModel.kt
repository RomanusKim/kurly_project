package com.example.kurly_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Section
import com.example.domain.usecase.GetProductsUseCase
import com.example.domain.usecase.GetSectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSectionsUseCase: GetSectionsUseCase,
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private var currentPage = 1
    private var isLastPage = false

    private val _sectionList = MutableStateFlow<List<Section>>(emptyList())
    val sectionList: StateFlow<List<Section>> = _sectionList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadInitialSections() {
        currentPage = 1
        isLastPage = false
        loadSections(reset = true)
    }

    fun loadNextPage() {
        if (!isLastPage && !_isLoading.value) {
            currentPage++
            loadSections(reset = false)
        }
    }

    private fun loadSections(reset: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val sections = getSectionsUseCase(currentPage)
                if (sections.isEmpty()) isLastPage = true

                _sectionList.update { currentList ->
                    if (reset) sections else currentList + sections
                }
            } catch (e: Exception) {
                Timber.e(e, "[ESES##] loadSections Error ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}