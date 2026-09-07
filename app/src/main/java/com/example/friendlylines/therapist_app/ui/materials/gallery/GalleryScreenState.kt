package com.example.friendlylines.therapist_app.ui.materials.gallery

import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem

data class GalleryScreenState(
    val patterns: List<PatternItem> = emptyList(),
    val hideExamplePatterns: Boolean = false,
    val patternToDelete: PatternItem? = null,
    val scrollToPatternId: Long? = null
)