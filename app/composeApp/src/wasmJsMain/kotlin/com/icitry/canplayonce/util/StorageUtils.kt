package com.icitry.canplayonce.util

import kotlinx.browser.localStorage

class BrowserLocalStorage : LocalStorage {
    override fun save(key: String, value: String) {
        localStorage.setItem(key, value)
    }

    override fun retrieve(key: String): String? {
        return localStorage.getItem(key)
    }
}

private var storage: LocalStorage? = null

actual fun initLocalStorage(context: Any?) {
    storage = BrowserLocalStorage()
}

actual fun getLocalStorage(): LocalStorage =
    storage ?: throw IllegalStateException("PlatformStorage not initialized")