package com.example.shared.data.drafts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ColorOption(
    val key: String,
    val color: Color
)

fun colorOptionFromKey(key: String?): ColorOption? {
    return when (key) {
        "red" -> ColorOption("red", Color(0xFFFF1744))
        "orange" -> ColorOption("orange", Color(0xFFFF9100))
        "yellow" -> ColorOption("yellow", Color(0xFFFFFF00))
        "green" -> ColorOption("green",  Color(0xFF00E676))
        "blue" -> ColorOption("blue", Color(0xFF00B0FF))
        "purple" -> ColorOption("purple", Color(0xFFD500F9))
        "pink" -> ColorOption("pink", Color(0xFFFF4081))

        "white" -> ColorOption("white", Color.White)
        "black" -> ColorOption("black", Color.Black)
        "pastel_red" -> ColorOption("pastel_red", Color(0xFFFFCDD2))
        "pastel_orange" -> ColorOption("pastel_orange", Color(0xFFFFE0B2))
        "pastel_yellow" -> ColorOption("pastel_yellow", Color(0xFFFFF9C4))
        "pastel_green" -> ColorOption("pastel_green", Color(0xFFC8E6C9))
        "pastel_blue" -> ColorOption("pastel_blue", Color(0xFFBBDEFB))
        "pastel_purple" -> ColorOption("pastel_purple", Color(0xFFE1BEE7))
        "pastel_pink" -> ColorOption("pastel_pink", Color(0xFFF8BBD0))

        null -> null
        else -> null
    }
}

enum class PatternWidth {
    THIN,
    MEDIUM,
    THICK
}

fun PatternWidth.toDp(): Dp {
    return when (this) {
        PatternWidth.THIN -> 12.dp
        PatternWidth.MEDIUM -> 24.dp
        PatternWidth.THICK -> 48.dp
    }
}