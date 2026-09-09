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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsViewModel

// Final screen of learning step configuration
@Composable
fun LearningStepsSummaryScreen(
    settingsViewModel: LearningStepsSettingsViewModel,
    onSaved: () -> Unit
) {
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
                    settingsViewModel.saveLearningStep(
                        onSaved = {
                            onSaved()
                        }
                    )
                },
                text = stringResource(R.string.save_button_text),
                icon = Icons.Default.Check
            )
        }
    }
}