package com.icitry.canplayonce.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

data class ContainerData(
    private var width: Float = 0f,
    private var height: Float = 0f
) {
    var widthState = mutableStateOf(width)
    var heightState = mutableStateOf(height)
}

val ContainerDataSaver = Saver<ContainerData, Map<String, Any>>(
    save = { player ->
        mapOf(
            "width" to player.widthState.value,
            "height" to player.heightState.value
        )
    },
    restore = { savedMap ->
        ContainerData(
            width = savedMap["width"] as Float,
            height = savedMap["height"] as Float
        )
    }
)