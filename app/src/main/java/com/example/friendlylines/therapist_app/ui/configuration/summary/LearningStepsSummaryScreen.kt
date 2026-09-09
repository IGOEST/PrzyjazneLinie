package com.example.friendlylines.therapist_app.ui.configuration.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsViewModel
import com.example.friendlylines.therapist_app.ui.components.NameError
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.friendlylines.therapist_app.ui.components.TemplateSaveDialog

// Final screen of learning step configuration
@Composable
fun LearningStepsSummaryScreen(
    settingsViewModel: LearningStepsSettingsViewModel,
    onSaved: () -> Unit
) {
    var showSaveDialog by remember {
        mutableStateOf(false)
    }

    var stepName by remember {
        mutableStateOf("")
    }

    var stepNameError by remember {
        mutableStateOf<NameError?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TemplateButton(
                modifier = Modifier.wrapContentWidth(),
                enabled = true,
                onClick = {
                    stepName = ""
                    stepNameError = null
                    showSaveDialog = true
                },
                text = stringResource(R.string.save_button_text),
                icon = Icons.Default.Check
            )
        }
    }

    if (showSaveDialog) {
        TemplateSaveDialog(
            title = stringResource(R.string.save_learning_step_dialog_title),
            confirmText = stringResource(R.string.save_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            textFieldLabel = stringResource(R.string.save_name_field_title),
            name = stepName,
            onNameChange = {
                stepName = it
                stepNameError = null
            },
            nameError = stepNameError,
            onDismiss = {
                showSaveDialog = false
                stepName = ""
                stepNameError = null
            },
            onSave = {
                val name = stepName.trim()

                if (name.isBlank()) {
                    stepNameError = NameError.BLANK
                    return@TemplateSaveDialog
                }

                settingsViewModel.saveLearningStep(
                    name = name,
                    onSaved = {
                        showSaveDialog = false
                        stepName = ""
                        stepNameError = null
                        onSaved()
                    }
                )
            }
        )
    }
}