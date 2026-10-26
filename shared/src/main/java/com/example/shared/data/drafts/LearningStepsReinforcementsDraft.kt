package com.example.shared.data.drafts

data class LearningStepsReinforcementsDraft(
    val enabledPraiseWords: Set<String> = PRAISE_WORDS,
    val praiseWordsEnabled: Boolean = true,
    val enabledAnimationThemes: Set<String> = ANIMATION_THEMES,
    val animationsEnabled: Boolean = true,
    val endSessionAnimationEnabled: Boolean = true,
    val endSessionFanfareEnabled: Boolean = true,
) {
    companion object {
        val PRAISE_WORDS: Set<String> =
            setOf(
                "dobrze",
                "super",
                "świetnie",
                "brawo",
            )
        val ANIMATION_THEMES: Set<String> =
            setOf(
                "flowers",
                "butterflies",
                "balloons",
                "cars",
                "balls",
            )
    }
}