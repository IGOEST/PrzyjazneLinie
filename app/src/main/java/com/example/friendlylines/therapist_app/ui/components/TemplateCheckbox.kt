package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesShapes
import com.example.friendlylines.theme.scaled

private val CHECKBOX_CHECK_ICON_SIZE = 18.dp

@Composable
fun TemplateCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultBorderColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
    checkedBorderColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
    disabledBorderColor: Color = FriendlyLinesColors.Neutral.N300,
    defaultBackgroundColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P50,
    checkedBackgroundColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
    disabledBackgroundColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P50,
    checkmarkColor: Color = Color.White
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val borderColor = when {
        !enabled -> disabledBorderColor
        checked -> checkedBorderColor
        else -> defaultBorderColor
    }

    val backgroundColor = when {
        !enabled -> disabledBackgroundColor
        checked -> checkedBackgroundColor
        else -> defaultBackgroundColor
    }

    val checkmarkColor = checkmarkColor

    Box(
        modifier = modifier
            .clip(FriendlyLinesShapes.extraSmall)
            .background(backgroundColor)
            .border(
                width = 2.dp.scaled(),
                color = borderColor,
                shape = FriendlyLinesShapes.extraSmall
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onCheckedChange(!checked)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(CHECKBOX_CHECK_ICON_SIZE.scaled())
            )
        }
    }
}