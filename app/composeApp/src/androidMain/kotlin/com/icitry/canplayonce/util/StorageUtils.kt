package com.icitry.canplayonce.util

import android.content.Context
import android.content.SharedPreferences
import com.icitry.canplayonce.LocalStorage

class AndroidLocalStorage(context: Context) : LocalStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("app_storage", Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun retrieve(key: String): String? {
        return sharedPreferences.getString(key, null)
    }
}

private var storage: LocalStorage? = null

actual fun initLocalStorage(context: Any?) {
    storage = AndroidLocalStorage(context as Context)
}

actual fun getLocalStorage(): LocalStorage =
    storage ?: throw IllegalStateException("PlatformStorage not initialized")