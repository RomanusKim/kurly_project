package com.example.kurly_project.data

import android.content.Context

class WishlistManager(context: Context) {
    private val prefs = context.getSharedPreferences("wishlist_prefs", Context.MODE_PRIVATE)

    fun isWished(productId: Long): Boolean =
        prefs.getBoolean(productId.toString(), false)

    fun toggleWished(productId: Long) {
        val current = isWished(productId)
        prefs.edit().putBoolean(productId.toString(), !current).apply()
    }
}