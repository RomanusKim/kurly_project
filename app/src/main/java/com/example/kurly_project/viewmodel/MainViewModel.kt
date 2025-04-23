package com.example.kurly_project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Section
import com.example.domain.usecase.GetProductsUseCase
import com.example.domain.usecase.GetSectionsUseCase
import com.example.kurly_project.data.SectionWithProducts
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

    /**
     * 페이징 처리: 마지막 페이지가 아니고, 로딩 중이 아닐 경우 다음 페이지 로드
     */
    fun loadNextPage() {
        if (_isLoading.value || isLastPage) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. 섹션 목록 + 다음 페이지 정보 가져오기
                val (sections, nextPage) = getSectionsUseCase(currentPage)

                // 2. 다음 페이지 갱신
                if (nextPage == null) {
                    isLastPage = true
                } else {
                    currentPage = nextPage
                }

                // 3. 각 섹션에 대응하는 상품 목록 불러오기
                val enrichedSections = sections.map { section ->
                    val products = getProductsUseCase(section.url)
                    SectionWithProducts(section, products)
                }

                // 4. 누적 저장
                _sectionWithProducts.update { prev ->
                    prev + enrichedSections
                }

            } catch (e: Exception) {
                Timber.e(e, "[ESES##] 섹션/상품 로딩 실패: ${e.localizedMessage}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}