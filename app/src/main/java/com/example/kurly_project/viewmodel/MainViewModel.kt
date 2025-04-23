package com.example.kurly_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Section
import com.example.domain.usecase.GetProductsUseCase
import com.example.domain.usecase.GetSectionsUseCase
import com.example.kurly_project.data.SectionWithProducts
import com.example.kurly_project.mapper.toUiModel
import com.example.kurly_project.model.ProductUiModel
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

    private val _sectionWithProducts = MutableStateFlow<List<SectionWithProducts>>(emptyList())
    val sectionWithProducts: StateFlow<List<SectionWithProducts>> = _sectionWithProducts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    /**
     * 첫 진입 시 초기화 후 첫 페이지 로드
     */
    fun loadSectionsAndProducts() {
        currentPage = 1
        isLastPage = false
        _sectionWithProducts.value = emptyList()
        loadNextPage()
    }

    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val (sections, nextPage) = getSectionsUseCase(currentPage)

                if (nextPage == null) isLastPage = true
                else currentPage = nextPage

                val enrichedSections = sections.map { section ->
                    val products = getProductsUseCase(section.url).map { it.toUiModel() }
                    SectionWithProducts(section, products)
                }

                _sectionWithProducts.update { prev -> prev + enrichedSections }

            } catch (e: Exception) {
                Timber.e(e, "[ESES##] 섹션/상품 로딩 실패: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}