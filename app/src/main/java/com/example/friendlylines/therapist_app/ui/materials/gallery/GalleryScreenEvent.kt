package com.example.friendlylines.therapist_app.ui.materials.gallery

import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem

sealed class GalleryScreenEvent {
    data class HideExamplePatternsChanged(val hide: Boolean) : GalleryScreenEvent()
    data class DeletePatternClicked(val pattern: PatternItem) : GalleryScreenEvent()
    data object DeletePatternConfirmed : GalleryScreenEvent()
    data object DeletePatternDismissed : GalleryScreenEvent()
    data class NewPatternReceived(val patternId: Long) : GalleryScreenEvent()
    data object ScrollToPattern: GalleryScreenEvent()
}