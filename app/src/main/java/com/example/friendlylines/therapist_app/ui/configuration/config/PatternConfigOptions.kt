package com.example.friendlylines.therapist_app.ui.configuration.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R

data class ColorOption(
    val nameRes: Int,
    val color: Color
)

enum class PatternWidth {
    THIN,
    MEDIUM,
    THICK
}

object PatternConfigOptions {
    val patternAndWriting = listOf(
        ColorOption(R.string.color_red, Color(0xFFFF1744)),
        ColorOption(R.string.color_orange, Color(0xFFFF9100)),
        ColorOption(R.string.color_yellow, Color(0xFFFFFF00)),
        ColorOption(R.string.color_green, Color(0xFF00E676)),
        ColorOption(R.string.color_blue, Color(0xFF00B0FF)),
        ColorOption(R.string.color_purple, Color(0xFFD500F9)),
        ColorOption(R.string.color_pink, Color(0xFFFF4081)),
    )


    val background = listOf(
        ColorOption(R.string.color_white, Color.White),
        ColorOption(R.string.color_black, Color.Black),
        ColorOption(R.string.color_pas_red, Color(0xFFFFCDD2)),
        ColorOption(R.string.color_pas_orange, Color(0xFFFFE0B2)),
        ColorOption(R.string.color_pas_yellow, Color(0xFFFFF9C4)),
        ColorOption(R.string.color_pas_green, Color(0xFFC8E6C9)),
        ColorOption(R.string.color_pas_blue, Color(0xFFBBDEFB)),
        ColorOption(R.string.color_pas_purple, Color(0xFFE1BEE7)),
        ColorOption(R.string.color_pas_pink, Color(0xFFF8BBD0))
    )

    fun PatternWidth.toDp(): Dp {
        return when (this) {
            PatternWidth.THIN -> 4.dp
            PatternWidth.MEDIUM -> 8.dp
            PatternWidth.THICK -> 16.dp
        }
    }
}