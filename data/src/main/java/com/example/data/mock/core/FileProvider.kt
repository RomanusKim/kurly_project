package com.example.data.mock.core

internal interface FileProvider {
    fun getJsonFromAsset(filePath: String): String?
}
