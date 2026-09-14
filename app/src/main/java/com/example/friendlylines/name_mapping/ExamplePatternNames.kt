package com.example.friendlylines.name_mapping

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R

object ExamplePatternNames {

    @Composable
    fun nameFor(key: String): String {
        return when (key) {
            "square" -> stringResource(R.string.example_pattern_square)
            "triangle" -> stringResource(R.string.example_pattern_triangle)
            "circle" -> stringResource(R.string.example_pattern_circle)
            else -> key
        }
    }
}