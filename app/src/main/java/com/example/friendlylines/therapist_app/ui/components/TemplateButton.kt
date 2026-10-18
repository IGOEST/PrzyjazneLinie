package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val BUTTON_HEIGHT = 54.dp
private val BUTTON_ICON_SIZE = 24.dp
private val BUTTON_ICON_TEXT_GAP = 10.dp
private val BUTTON_CONTENT_PADDING_HORIZONTAL = 20.dp
private val BUTTON_CONTENT_PADDING_VERTICAL = 12.dp
private val BUTTON_SHADOW_ELEVATION = 2.dp

@Composable
fun TemplateButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    isDialogButton: Boolean = false,
    onClick: () -> Unit,
    text: String,
    icon: ImageVector?,
    defaultColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
    activeColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
    disabledColor: Color = FriendlyLinesColors.Neutral.N300,
    defaultContentColor: Color = FriendlyLinesColors.Shades.White,
    activeContentColor: Color = FriendlyLinesColors.Shades.White,
    disabledContentColor: Color = FriendlyLinesColors.Shades.White
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> disabledColor
        isPressed -> activeColor
        else -> defaultColor
    }

    val contentColor = when {
        !enabled -> disabledContentColor
        isPressed -> activeContentColor
        else -> defaultContentColor
    }

    Box(
        modifier = modifier
            .then(
                if (!isDialogButton) {
                    Modifier
                        .shadow(
                            elevation = BUTTON_SHADOW_ELEVATION,
                            shape = FriendlyLinesModalShape
                        )
                } else {
                    Modifier
                }
            )
            .background(
                color = backgroundColor,
                shape = FriendlyLinesModalShape
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .heightIn(min = BUTTON_HEIGHT.scaled()),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = BUTTON_CONTENT_PADDING_HORIZONTAL.scaled(),
                vertical = BUTTON_CONTENT_PADDING_VERTICAL.scaled()
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(BUTTON_ICON_SIZE.scaled())
                )
                Spacer(modifier = Modifier.width(BUTTON_ICON_TEXT_GAP.scaled()))
            }

            Text(
                text = text,
                color = contentColor,
                style = FriendlyLinesTextStyles.button,
                maxLines = 2,
                softWrap = true,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}