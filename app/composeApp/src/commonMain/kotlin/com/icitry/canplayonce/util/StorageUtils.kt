package com.icitry.canplayonce.util

interface LocalStorage {
    fun save(key: String, value: String)
    fun retrieve(key: String): String?
}

expect fun initLocalStorage(context: Any? = null)

expect fun getLocalStorage(): LocalStorage
