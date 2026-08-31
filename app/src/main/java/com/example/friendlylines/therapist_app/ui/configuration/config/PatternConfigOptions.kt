package com.example.friendlylines.therapist_app.ui.configuration.config

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ColorOption(
    val name: String,
    val color: Color
)

enum class PatternWidth {
    THIN,
    MEDIUM,
    THICK
}

object PatternConfigOptions {

    val patternAndWriting = listOf(
        ColorOption("Czerwony", Color(0xFFFF1744)),
        ColorOption("Pomarańczowy", Color(0xFFFF9100)),
        ColorOption("Żółty", Color(0xFFFFFF00)),
        ColorOption("Zielony", Color(0xFF00E676)),
        ColorOption("Niebieski", Color(0xFF00B0FF)),
        ColorOption("Fioletowy", Color(0xFFD500F9)),
        ColorOption("Różowy", Color(0xFFFF4081))
    )

    val background = listOf(
        ColorOption("Biały", Color.White),
        ColorOption("Czarny", Color.Black),
        ColorOption("Pastelowy czerwony", Color(0xFFFFCDD2)),
        ColorOption("Pastelowy pomarańczowy", Color(0xFFFFE0B2)),
        ColorOption("Pastelowy żółty", Color(0xFFFFF9C4)),
        ColorOption("Pastelowy zielony", Color(0xFFC8E6C9)),
        ColorOption("Pastelowy niebieski", Color(0xFFBBDEFB)),
        ColorOption("Pastelowy fioletowy", Color(0xFFE1BEE7)),
        ColorOption("Pastelowy różowy", Color(0xFFF8BBD0))
    )

    fun PatternWidth.toDp(): Dp {
        return when (this) {
            PatternWidth.THIN -> 4.dp
            PatternWidth.MEDIUM -> 8.dp
            PatternWidth.THICK -> 16.dp
        }
    }
}