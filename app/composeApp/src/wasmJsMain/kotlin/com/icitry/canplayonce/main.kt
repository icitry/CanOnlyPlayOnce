package com.icitry.canplayonce

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.icitry.canplayonce.util.initLocalStorage
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    Napier.base(DebugAntilog())

    initLocalStorage()

    ComposeViewport(document.body!!) {
        MainApp()
    }
}