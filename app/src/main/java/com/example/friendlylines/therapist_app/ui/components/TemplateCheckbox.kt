package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700

@Composable
fun TemplateCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    defaultBorderColor: Color = Primary700,
    checkedBorderColor: Color = Primary700,
    disabledBorderColor: Color = Neutral300,
    defaultBackgroundColor: Color = Primary50,
    checkedBackgroundColor: Color = Primary700,
    disabledBackgroundColor: Color = Primary50,
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
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(4.dp)
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
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = checkmarkColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}