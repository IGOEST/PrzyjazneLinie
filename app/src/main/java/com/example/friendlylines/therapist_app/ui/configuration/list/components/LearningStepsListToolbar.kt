package com.example.friendlylines.therapist_app.ui.configuration.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateSearchBox

@Composable
fun LearningStepsListToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier,
    hideExampleSteps: Boolean,
    onHideExampleStepsChange: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
        ) {
            TemplateButton(
                enabled = true,
                onClick = onCreateClick,
                text = stringResource(R.string.create_button_text),
                icon = Icons.Filled.AddCircleOutline
            )
            InfoIconButton(
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_step_create_new_info_message),
                defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                disabledTint = FriendlyLinesColors.Neutral.N300
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
        ) {
            InfoIconButton(
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_play_info_message),
                defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                disabledTint = FriendlyLinesColors.Neutral.N300
            )
            TemplateButton(
                enabled = true,
                text = stringResource(R.string.learning_steps_play_button_text),
                icon = Icons.Filled.PlayCircleFilled,
                onClick = onPlayClick,
            )
        }
    }
    TemplateSearchBox(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = stringResource(R.string.search_box_text)
    )
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())) {
        TemplateCheckbox(
            checked = hideExampleSteps,
            onCheckedChange = onHideExampleStepsChange,
            modifier = Modifier.size(24.dp.scaled())
        )
        Text(
            text = stringResource(R.string.hide_exemplary_steps),
            style = FriendlyLinesTextStyles.captionC1,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
        )
        InfoIconButton(
            infoTitle = stringResource(R.string.info_dialog_title),
            infoMessage = stringResource(R.string.hide_exemplary_steps_info_message),
            defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
            activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
            disabledTint = FriendlyLinesColors.Neutral.N300
        )
    }
}