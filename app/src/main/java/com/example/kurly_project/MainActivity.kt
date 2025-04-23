package com.example.kurly_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurly_project.databinding.ActivityMainBinding
import com.example.kurly_project.ui.adapter.SectionProductAdapter
import com.example.kurly_project.ui.adapter.ViewType
import com.example.kurly_project.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 섹션 + 상품 로드
        viewModel.loadSectionsAndProducts()

        lifecycleScope.launchWhenStarted {
            viewModel.sectionWithProducts.collect { sections ->
                Timber.d("[ESES##] 섹션 수: ${sections.size}")
                sections.forEach { sectionWithProducts ->
                    val section = sectionWithProducts.section
                    val products = sectionWithProducts.products

                    when (section.type) {
                        "horizontal" -> {
                            binding.sectionOneTitle.text = section.title
                            binding.sectionOneRecyclerView.apply {
                                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                adapter = SectionProductAdapter(
                                    context = context,
                                    items = products.toMutableList(),
                                    viewType = ViewType.HORIZONTAL)
                            }
                        }

                        "grid" -> {
                            binding.sectionTwoTitle.text = section.title
                            binding.sectionTwoRecyclerView.apply {
                                layoutManager = GridLayoutManager(context, 3)
                                setHasFixedSize(true)
                                adapter = SectionProductAdapter(
                                    context = context,
                                    items = products.toMutableList(),
                                    viewType = ViewType.GRID)
                            }
                        }

                        "vertical" -> {
                            binding.sectionThreeTitle.text = section.title
                            binding.sectionThreeRecyclerView.apply {
                                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                                adapter = SectionProductAdapter(
                                    context = context,
                                    items = products.toMutableList(),
                                    viewType = ViewType.VERTICAL)
                            }
                        }
                    }
                }
            }
        }

        // ✅ 다음 페이지 로드
        binding.nestedScrollView.setOnScrollChangeListener { v, _, scrollY, _, _ ->
            val view = v as NestedScrollView
            val isBottom = scrollY >= (view.getChildAt(0).measuredHeight - view.height)

            if (isBottom && !viewModel.isLoading.value) {
                Timber.d("[ESES##] 🔄 스크롤 끝 → 다음 페이지 요청")
                viewModel.loadNextPage()
            }
        }

        // ✅ 새로고침
        binding.layoutSwipeRefreshLayout.setOnRefreshListener {
            Timber.d("[ESES##] 🔃 SwipeRefresh → 초기화 시작")
            viewModel.loadSectionsAndProducts()
        }

        // ✅ 로딩 상태 감지 후 SwipeRefresh indicator 끄기
        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collectLatest { loading ->
                binding.layoutSwipeRefreshLayout.isRefreshing = loading
            }
        }
    }
}