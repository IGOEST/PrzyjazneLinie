package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary700

@Composable
fun TemplateToggleSwitch(
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 60.dp,
    height: Dp = 15.dp,
    circleSize: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .width(width)
            .height(circleSize)
            .clickable (enabled = enabled) {
                onCheckedChange(!checked)
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(width)
                .height(height)
                .align(
                    if (checked) {
                        Alignment.CenterStart
                    } else {
                        Alignment.CenterEnd
                    }
                )
                .background(
                    color = when {
                        !enabled -> Neutral300
                        checked -> Primary300
                        else -> Neutral300
                    },
                    shape = RoundedCornerShape(height / 2)
                )
        )

        Box(
            modifier = Modifier
                .size(circleSize)
                .align(
                    if (checked) {
                        Alignment.CenterEnd
                    } else {
                        Alignment.CenterStart
                    }
                )
                .shadow(
                    elevation = 10.dp,
                    shape = CircleShape
                )
                .background(
                    color = when {
                        !enabled -> Neutral300
                        checked -> Primary700
                        else -> Color.White
                    },
                    shape = CircleShape
                )
        )
    }
}