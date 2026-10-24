package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.scaled

@Composable
fun InfoIconButton(
    infoTitle: String?,
    infoMessage: String?,
    modifier: Modifier = Modifier,
    iconSize: Dp = 24.dp.scaled(),
    defaultTint: Color,
    activeTint: Color,
    disabledTint: Color,
    enabled: Boolean = true
) {
    var showInfo by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        TemplateClickableIcon(
            onClick = { showInfo = true },
            icon = Icons.Rounded.Info,
            contentDescription = null,
            modifier = modifier.size(iconSize),
            defaultTint = defaultTint,
            activeTint = activeTint,
            disabledTint = disabledTint,
            enabled = enabled
        )
    }

    if (showInfo) {
        TemplateInfoDialog(
            title = infoTitle,
            message = infoMessage,
            onDismiss = { showInfo = false }
        )
    }
}