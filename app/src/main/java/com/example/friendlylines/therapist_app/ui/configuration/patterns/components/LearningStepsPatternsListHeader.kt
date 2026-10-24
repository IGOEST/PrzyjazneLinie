package com.example.friendlylines.therapist_app.ui.configuration.patterns.components

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
import com.example.friendlylines.therapist_app.ui.configuration.list.components.ROW_HORIZONTAL_PADDING

@Composable
fun LearningStepsPatternsListHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ROW_HORIZONTAL_PADDING.scaled()),
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            HeaderLabel(
                text = stringResource(R.string.pattern_list_header_name),
                infoTitle = null,
                infoMessage = null
            )
        }

        Row(
            modifier = Modifier.width(ROW_MODE_SECTION_WIDTH.scaled()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                HeaderLabel(
                    text = stringResource(R.string.patterns_list_header_learning),
                    infoTitle = null,
                    infoMessage = null
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                HeaderLabel(
                    text = stringResource(R.string.patterns_list_header_test),
                    infoTitle = null,
                    infoMessage = null
                )
            }
        }

        Box(
            modifier = Modifier.width(ROW_ACTIONS_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center
        ) {
            HeaderLabel(
                text = stringResource(R.string.list_header_actions),
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.pattern_list_header_actions_label_info_message),
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