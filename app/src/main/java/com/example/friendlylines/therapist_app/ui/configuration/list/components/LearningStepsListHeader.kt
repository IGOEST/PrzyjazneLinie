package com.example.friendlylines.therapist_app.ui.configuration.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

@Composable
fun LearningStepsListHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ROW_ITEM_SPACING.scaled()),
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = ROW_HORIZONTAL_PADDING.scaled()),
    ) {
        HeaderLabel(
            text = stringResource(R.string.learning_steps_header),
            infoTitle = stringResource(R.string.info_dialog_title),
            infoMessage = stringResource(R.string.learning_steps_header_label_info_message),
            modifier = Modifier.weight(1f)
        )
        Box(
            modifier = Modifier.width(ROW_MODE_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center,
        ) {
            HeaderLabel(
                text = stringResource(R.string.learning_steps_header_mode_label),
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_header_mode_label_info_meassage),
            )
        }
        Box(
            modifier = Modifier.width(ROW_ACTIONS_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center,
        ) {
            HeaderLabel(
                text = stringResource(R.string.list_header_actions),
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_header_actions_label_info_meassage),
            )
        }
    }
}

@Composable
private fun HeaderLabel(
    text: String,
    modifier: Modifier = Modifier,
    infoTitle: String? = null,
    infoMessage: String? = null,
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Text(
            text = text,
            style = FriendlyLinesTextStyles.captionC1,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
        )
        if (infoTitle != null && infoMessage != null) {
            InfoIconButton(
                infoTitle = infoTitle,
                infoMessage = infoMessage,
                modifier = Modifier.padding(start = 8.dp.scaled()),
                defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                disabledTint = FriendlyLinesColors.Neutral.N300,
            )
        }
    }
}