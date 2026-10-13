package com.example.friendlylines.therapist_app.ui.configuration.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.LearningStepSummaryBox
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsState
import com.example.friendlylines.therapist_app.ui.theme.Error
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.materials.models.NameError

@Composable
fun LearningStepsSummaryScreen(
    onSaveClick: () -> Unit,
    state: LearningStepsSettingsState,
    onEvent: (LearningStepsSummaryEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp
                )
        ) {
            LearningStepSummaryBox(
                state = state,
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(32.dp))


            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ){
                OutlinedTextField(
                    value = state.stepName,
                    onValueChange = {
                        onEvent(
                            LearningStepsSummaryEvent.NameChanged(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.enter_step_name) + ":",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary700,
                        unfocusedBorderColor = Neutral300,
                        focusedLabelColor = Primary1000,
                        unfocusedLabelColor = Neutral300,
                        errorBorderColor = Error,
                        errorLabelColor = Error,
                        cursorColor = Primary1000
                    ),
                    singleLine = true,
                    isError = state.stepNameError != null,
                    supportingText = {
                        state.stepNameError?.let { error ->
                            Text(
                                text = when (error) {
                                    NameError.BLANK -> stringResource(R.string.name_blank_error)
                                    NameError.EXISTS -> stringResource(R.string.name_exists_error)
                                },
                                color = Error,
                                fontSize = 16.7.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 4.dp
                                    ),
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TemplateButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    enabled = state.stepName.isNotBlank(),
                    onClick = onSaveClick,
                    text = stringResource(R.string.save_button_text),
                    icon = Icons.Filled.Save
                )
            }
        }
    }
}