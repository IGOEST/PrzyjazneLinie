package com.example.friendlylines.name_mapping

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R

object ExamplePatternNames {

    @Composable
    fun examplePatternNameFor(key: String): String {
        return when (key) {
            "square" -> stringResource(R.string.example_pattern_square)
            "triangle" -> stringResource(R.string.example_pattern_triangle)
            "circle" -> stringResource(R.string.example_pattern_circle)
            "vertical_line" -> stringResource(R.string.example_pattern_vertical_line)
            "horizontal_line" -> stringResource(R.string.example_pattern_horizontal_line)
            "diagonal_line_down" -> stringResource(R.string.example_pattern_diagonal_line_down)
            "diagonal_line_up" -> stringResource(R.string.example_pattern_diagonal_line_up)
            "curve_line" -> stringResource(R.string.example_pattern_curve_line)
            "ellipse" -> stringResource(R.string.example_pattern_ellipse)
            "rectangle" -> stringResource(R.string.example_pattern_rectangle)
            else -> key
        }
    }
}