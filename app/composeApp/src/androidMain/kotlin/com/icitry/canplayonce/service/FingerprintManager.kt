package com.icitry.canplayonce.service

actual object FingerprintManager {
    actual var userFingerprint: String? = null
        private set

    actual fun startUserFingerprinting(onComplete: (String) -> Unit) {

    }
}