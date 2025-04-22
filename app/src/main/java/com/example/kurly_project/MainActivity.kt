package com.example.kurly_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.domain.model.Product
import com.example.domain.model.Section
import com.example.kurly_project.databinding.ActivityMainBinding
import com.example.kurly_project.ui.adapter.SectionProductAdapter
import com.example.kurly_project.ui.adapter.ViewType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var isLoading = false
    var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionList = loadSectionListFromAssets()

        sectionList.forEach { section ->
            val product = loadProductListFromAssets(section.url)

            when (section.type) {
                "horizontal" -> {
                    binding.sectionOneTitle.text = section.title
                    binding.sectionOneRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    binding.sectionOneRecyclerView.adapter = SectionProductAdapter(product, ViewType.HORIZONTAL)
                }

                "grid" -> {
                    binding.sectionTwoTitle.text = section.title
                    binding.sectionTwoRecyclerView.layoutManager = GridLayoutManager(this, 3)
                    binding.sectionTwoRecyclerView.adapter = SectionProductAdapter(product, ViewType.GRID)
                }

                "vertical" -> {
                    binding.sectionThreeTitle.text = section.title
                    binding.sectionThreeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.sectionThreeRecyclerView.adapter = SectionProductAdapter(product, ViewType.VERTICAL)
                }
            }
        }

    }


    private fun loadSectionListFromAssets(): List<Section> {
        val inputStream = assets.open("sections/sections_1.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(json)
        val dataArray = jsonObject.getJSONArray("data")

        val gson = Gson()
        val type = object : TypeToken<List<Section>>() {}.type
        return gson.fromJson(dataArray.toString(), type)
    }

    private fun loadProductListFromAssets(url: String): List<Product> {
        val inputStream = assets.open("section/products/$url.json")
        val json = inputStream.bufferedReader().use { it.readText() }
        val root = JSONObject(json)
        val dataArray = root.getJSONArray("data")
        val type = object : TypeToken<List<Product>>() {}.type
        return Gson().fromJson(dataArray.toString(), type)
    }

    private fun setupScrollListener() {
        binding.apply {
            nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val view = v as NestedScrollView
                val diff = view.getChildAt(0).bottom - (view.height + scrollY)

                if (diff <= 100 && !isLoading) {
                    isLoading = true
                    currentPage += 1
                    // loadNextPage()
                }
            }
        }
    }
}