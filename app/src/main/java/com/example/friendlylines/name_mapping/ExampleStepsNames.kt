package com.example.friendlylines.name_mapping

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R

object  ExampleStepsNames {
    @Composable
    fun exampleLearningStepNameFor(key: String): String {
        return when (key) {
            "easy" -> stringResource(R.string.example_learning_step_easy)
            "medium" -> stringResource(R.string.example_learning_step_medium)
            "hard" -> stringResource(R.string.example_learning_step_hard)
            else -> key
        }
    }
}