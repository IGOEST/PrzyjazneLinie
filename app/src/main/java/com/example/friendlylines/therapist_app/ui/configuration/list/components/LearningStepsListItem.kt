package com.example.friendlylines.therapist_app.ui.configuration.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.name_mapping.ExampleStepsNames
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.shared.data.entities.LearningStepEntity

val ROW_HORIZONTAL_PADDING = 20.dp
val ROW_ITEM_SPACING = 18.dp
val ROW_CHECKBOX_SLOT_WIDTH = 48.dp
val ROW_MODE_SECTION_WIDTH = 220.dp

private val ROW_VERTICAL_PADDING = 10.dp
private val ROW_SHADOW_ELEVATION = 2.dp
private val ROW_MODE_TEXT_SPACING = 8.dp
private val ROW_ACTION_ICON_SIZE = 24.dp
private val ROW_ICON_BUTTON_TOUCH_SIZE = 48.dp
private val ROW_ACTION_ICON_SPACING = 8.dp

val ROW_ACTIONS_SECTION_WIDTH = ROW_ICON_BUTTON_TOUCH_SIZE * 2 + ROW_ACTION_ICON_SPACING * 2

@Composable
fun LearningStepsListItem(
    learningStep: LearningStepEntity,
    onEnabledChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onModeChange: (Boolean) -> Unit,
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
                color = if (learningStep.isActive) FriendlyLinesColors.PrimaryFriendlyLines.P500 else FriendlyLinesColors.Shades.White,
                shape = FriendlyLinesModalShape
            )
            .padding(
                horizontal = ROW_HORIZONTAL_PADDING.scaled(),
                vertical = ROW_VERTICAL_PADDING.scaled()
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ROW_ITEM_SPACING.scaled())

    ) {
        TemplateCheckbox(
            checked = learningStep.isActive,
            onCheckedChange = onEnabledChange,
            modifier = Modifier.size(18.dp.scaled())
        )
        Text(
            text = if (learningStep.isExample) {
                ExampleStepsNames.exampleLearningStepNameFor(learningStep.name)
            } else {
                learningStep.name
            },
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.headingH5Regular,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )

        Box(
            modifier = Modifier.width(ROW_MODE_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ROW_MODE_TEXT_SPACING.scaled()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.learning_mode),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.bodyRegular
                )

                TemplateToggleSwitch(
                    checked = learningStep.isTest,
                    onCheckedChange = {
                        onModeChange(it)
                    }
                )

                Text(
                    text = stringResource(R.string.test_mode),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.bodyRegular
                )
            }
        }

        Box(
            modifier = Modifier.width(ROW_ACTIONS_SECTION_WIDTH.scaled()),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ROW_ACTION_ICON_SPACING.scaled())
            ) {
                TemplateClickableIcon(
                    icon = Icons.Filled.Edit,
                    contentDescription = stringResource(R.string.edit_icon_description),
                    onClick = onEditClick,
                    modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled()),
                    enabled = !learningStep.isExample
                )

                TemplateClickableIcon(
                    icon = Icons.Filled.ContentCopy,
                    contentDescription = stringResource(R.string.copy_icon_description),
                    onClick = onCopyClick,
                    modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled())
                )

                TemplateClickableIcon(
                    icon = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_icon_description),
                    onClick = onDeleteClick,
                    modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled()),
                    enabled = !learningStep.isExample
                )
            }
        }
    }
}

@Composable
fun LearningStepsListItemAdd(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

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
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ROW_ITEM_SPACING.scaled())
    ) {
        TemplateClickableIcon(
            icon = Icons.Filled.AddCircleOutline,
            contentDescription = null,
            onClick = onClick,
            modifier = Modifier.size(ROW_ACTION_ICON_SIZE.scaled())
        )

        Text(
            text = stringResource(R.string.create_learning_step),
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.headingH5Regular,
        )
    }
}