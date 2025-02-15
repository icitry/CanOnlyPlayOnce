package com.icitry.canplayonce.service

expect object FingerprintManager {
    var userFingerprint: String?
        private set

    fun startUserFingerprinting(onComplete: (String) -> Unit)
}