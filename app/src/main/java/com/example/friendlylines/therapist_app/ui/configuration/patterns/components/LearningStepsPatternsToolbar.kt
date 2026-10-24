package com.example.friendlylines.therapist_app.ui.configuration.patterns.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateSearchBox

@Composable
fun LearningStepsPatternsToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddPatternClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
        ) {
            TemplateButton(
                enabled = true,
                onClick = onAddPatternClick,
                text = stringResource(R.string.add_pattern_button_text),
                icon = Icons.Filled.AddCircleOutline
            )
            InfoIconButton(
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.pattern_add_button_info_message),
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
            TemplateButton(
                enabled = true,
                text = stringResource(R.string.next_button_text),
                icon = Icons.Filled.ArrowForwardIos,
                onClick = onNextClick,
            )
        }
    }
    TemplateSearchBox(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = stringResource(R.string.search_box_text)
    )
}