package com.example.friendlylines.therapist_app.ui.configuration.patterns.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft

private val ROW_HORIZONTAL_PADDING = 20.dp
private val ROW_VERTICAL_PADDING = 10.dp
private val ROW_SHADOW_ELEVATION = 2.dp
private val ROW_ACTION_ICON_SIZE = 24.dp
private val ROW_ACTION_ICON_SPACING = 8.dp
val ROW_MODE_SECTION_WIDTH = 220.dp
val ROW_ACTIONS_SECTION_WIDTH = 300.dp

@Composable
fun LearningStepsPatternListItem(
    index: Int,
    pattern: LearningStepsPatternConfigDraft,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onLearningToggled: (Boolean) -> Unit,
    onTestToggled: (Boolean) -> Unit,
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = ROW_SHADOW_ELEVATION.scaled(),
                shape = FriendlyLinesModalShape
            )
            .background(
                color = FriendlyLinesColors.Shades.White,
                shape = FriendlyLinesModalShape
            )
            .padding(
                horizontal = ROW_HORIZONTAL_PADDING.scaled(),
                vertical = ROW_VERTICAL_PADDING.scaled()
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ROW_ACTION_ICON_SPACING.scaled()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TemplateCheckbox(
                    checked = pattern.isEnabled,
                    onCheckedChange = onEnabledChange,
                    modifier = Modifier.size(18.dp.scaled())
                )

                Text(
                    text = stringResource(R.string.pattern_text) + " ${pattern.order + 1} (${pattern.pattern.pattern.name})",
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.headingH5Regular,
                    maxLines = 1
                )
            }
        }

        Row(
            modifier = Modifier.width(ROW_MODE_SECTION_WIDTH.scaled()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TemplateCheckbox(
                    checked = pattern.isLearning,
                    onCheckedChange = onLearningToggled,
                    modifier = Modifier.size(18.dp.scaled()),
                    enabled = pattern.isEnabled
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                TemplateCheckbox(
                    checked = pattern.isTest,
                    onCheckedChange = onTestToggled,
                    modifier = Modifier.size(18.dp.scaled()),
                    enabled = pattern.isEnabled
                )
            }
        }

        Box(
            modifier = Modifier.width(ROW_ACTIONS_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(64.dp.scaled()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(ROW_ACTION_ICON_SPACING.scaled()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TemplateClickableIcon(
                        icon = Icons.Filled.ArrowDropUp,
                        contentDescription = null,
                        onClick = onMoveUpClick,
                        enabled = canMoveUp,
                        modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled()),
                        disabledTint = FriendlyLinesColors.PrimaryFriendlyLines.P300
                    )

                    TemplateClickableIcon(
                        icon = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        onClick = onMoveDownClick,
                        enabled = canMoveDown,
                        modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled()),
                        disabledTint = FriendlyLinesColors.PrimaryFriendlyLines.P300
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(ROW_ACTION_ICON_SPACING.scaled()),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TemplateClickableIcon(
                        icon = Icons.Filled.Edit,
                        contentDescription = null,
                        onClick = onEditClick,
                        modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled())
                    )

                    TemplateClickableIcon(
                        icon = Icons.Filled.ContentCopy,
                        contentDescription = null,
                        onClick = onCopyClick,
                        modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled())
                    )

                    TemplateClickableIcon(
                        icon = Icons.Filled.Delete,
                        contentDescription = null,
                        onClick = onDeleteClick,
                        modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled())
                    )
                }
            }
        }
    }
}