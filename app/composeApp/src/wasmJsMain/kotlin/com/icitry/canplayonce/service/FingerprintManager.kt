package com.icitry.canplayonce.service

import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import kotlin.js.Promise

@JsModule("./fingerprint.js")
external object FingerprintingScript {
    fun getCombinedFingerprint(): Promise<JsAny?>
}

actual object FingerprintManager {
    private val TAG = FingerprintManager::class.simpleName

    actual var userFingerprint: String? = null
        private set

    actual fun startUserFingerprinting(onComplete: (String) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val fingerprint = FingerprintingScript.getCombinedFingerprint().await<JsAny?>()
                fingerprint?.let {
                    userFingerprint = it.toString()
                    onComplete(userFingerprint!!)
                }
            } catch (e: Exception) {
                Napier.d { "$TAG: ${e.message}" }
            }
        }
    }

}