package com.icitry.canplayonce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.icitry.canplayonce.util.initLocalStorage
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Napier.base(DebugAntilog())

        initLocalStorage(applicationContext)

        setContent {
            MainApp()
        }
    }
}
