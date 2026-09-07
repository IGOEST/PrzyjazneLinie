package com.example.shared.data.models

data class Pattern(
    val id: Long = 0,
    val name: String,
    val isExample: Boolean,
    val isComplex: Boolean,
    val smoothingEnabled: Boolean,
    val strokes: List<DrawingStroke>,
)