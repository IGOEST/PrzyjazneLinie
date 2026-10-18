package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled

private val SWITCH_WIDTH = 38.dp
private val SWITCH_HEIGHT = 15.dp
private val SWITCH_CIRCLE_SIZE = 24.dp
private val SWITCH_SHADOW_ELEVATION = 10.dp

@Composable
fun TemplateToggleSwitch(
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Box(
        modifier = modifier
            .width(SWITCH_WIDTH.scaled())
            .height(SWITCH_CIRCLE_SIZE.scaled())
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = { onCheckedChange(!checked) }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(SWITCH_WIDTH.scaled())
                .height(SWITCH_HEIGHT.scaled())
                .align(
                    if (checked) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }
                )
                .background(
                    color = when {
                        !enabled -> FriendlyLinesColors.Neutral.N300
                        checked -> FriendlyLinesColors.PrimaryFriendlyLines.P300
                        else -> FriendlyLinesColors.Neutral.N300
                    },
                    shape = RoundedCornerShape((SWITCH_HEIGHT / 2).scaled())
                )
        )

        Box(
            modifier = Modifier
                .size(SWITCH_CIRCLE_SIZE.scaled())
                .align(
                    if (checked) {
                        Alignment.CenterEnd
                    } else {
                        Alignment.CenterStart
                    }
                )
                .shadow(
                    elevation = SWITCH_SHADOW_ELEVATION.scaled(),
                    shape = CircleShape
                )
                .background(
                    color = when {
                        !enabled -> FriendlyLinesColors.Shades.White
                        checked -> FriendlyLinesColors.PrimaryFriendlyLines.P700
                        else -> FriendlyLinesColors.Shades.White
                    },
                    shape = CircleShape
                )
        )
    }
}