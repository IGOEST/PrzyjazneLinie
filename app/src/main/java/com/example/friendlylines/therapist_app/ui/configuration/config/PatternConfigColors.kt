package com.example.friendlylines.therapist_app.ui.configuration.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R
import com.example.shared.data.drafts.ColorOption

object PatternConfigColors {

    @Composable
    fun patternAndWriting(): List<ColorOption> {
        return listOf(
            ColorOption(
                key = "red",
                color = Color(0xFFFF1744)
            ),
            ColorOption(
                key = "orange",
                color = Color(0xFFFF9100)
            ),
            ColorOption(
                key = "yellow",
                color = Color(0xFFFFFF00)
            ),
            ColorOption(
                key = "green",
                color = Color(0xFF00E676)
            ),
            ColorOption(
                key = "blue",
                color = Color(0xFF00B0FF)
            ),
            ColorOption(
                key = "purple",
                color = Color(0xFFD500F9)
            ),
            ColorOption(
                key = "pink",
                color = Color(0xFFFF4081)
            )
        )
    }

    @Composable
    fun background(): List<ColorOption> {
        return listOf(
            ColorOption(
                key = "white",
                color = Color.White
            ),
            ColorOption(
                key = "black",
                color = Color.Black
            ),
            ColorOption(
                key = "pastel_red",
                color = Color(0xFFFFCDD2)
            ),
            ColorOption(
                key = "pastel_orange",
                color = Color(0xFFFFE0B2)
            ),
            ColorOption(
                key = "pastel_yellow",
                color = Color(0xFFFFF9C4)
            ),
            ColorOption(
                key = "pastel_green",
                color = Color(0xFFC8E6C9)
            ),
            ColorOption(
                key = "pastel_blue",
                color = Color(0xFFBBDEFB)
            ),
            ColorOption(
                key = "pastel_purple",
                color = Color(0xFFE1BEE7)
            ),
            ColorOption(
                key = "pastel_pink",
                color = Color(0xFFF8BBD0)
            )
        )
    }

    @Composable
    fun nameFor(key: String): String {
        return when (key) {
            "red" -> stringResource(R.string.color_red)
            "orange" -> stringResource(R.string.color_orange)
            "yellow" -> stringResource(R.string.color_yellow)
            "green" -> stringResource(R.string.color_green)
            "blue" -> stringResource(R.string.color_blue)
            "purple" -> stringResource(R.string.color_purple)
            "pink" -> stringResource(R.string.color_pink)

            "white" -> stringResource(R.string.color_white)
            "black" -> stringResource(R.string.color_black)
            "pastel_red" -> stringResource(R.string.color_pas_red)
            "pastel_orange" -> stringResource(R.string.color_pas_orange)
            "pastel_yellow" -> stringResource(R.string.color_pas_yellow)
            "pastel_green" -> stringResource(R.string.color_pas_green)
            "pastel_blue" -> stringResource(R.string.color_pas_blue)
            "pastel_purple" -> stringResource(R.string.color_pas_purple)
            "pastel_pink" -> stringResource(R.string.color_pas_pink)

            else -> ""
        }
    }
}