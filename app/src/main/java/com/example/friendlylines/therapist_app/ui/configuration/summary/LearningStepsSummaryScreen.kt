package com.example.friendlylines.therapist_app.ui.configuration.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.LearningStepSummaryBox
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateDropdown
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsState
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsViewModel
import com.example.friendlylines.therapist_app.ui.materials.create_new.PatternNameError
import com.example.friendlylines.therapist_app.ui.materials.models.PatternPreview
import com.example.friendlylines.therapist_app.ui.theme.Error
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.shared.data.drafts.PatternWidth

enum class LearningStepNameError {
    BLANK,
    EXISTS
}

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
                            text = "Wpisz nazwę kroku:",
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
                                    LearningStepNameError.BLANK -> stringResource(R.string.name_blank_error)
                                    LearningStepNameError.EXISTS -> stringResource(R.string.name_exists_error)
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