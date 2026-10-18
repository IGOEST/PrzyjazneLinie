package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

@Composable
fun TemplateInfoDialog(
    icon: ImageVector = Icons.Default.Info,
    title: String?,
    message: String?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = FriendlyLinesModalShape,
        containerColor = FriendlyLinesColors.Shades.White,
        title = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = FriendlyLinesColors.Neutral.N400,
                )
                if (title != null) {
                    Text(
                        text = title,
                        style = FriendlyLinesTextStyles.headingH5Medium,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                    )
                }
            }
        },
        text = {
            if (message != null) {
                Text(
                    text = message,
                    style = FriendlyLinesTextStyles.bodyRegular,
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                )
            }
        },
        confirmButton = {
            TemplateButton(
                modifier = Modifier.wrapContentWidth(),
                enabled = true,
                isDialogButton = true,
                onClick = onDismiss,
                text = stringResource(android.R.string.ok),
                icon = null,
                defaultColor = FriendlyLinesColors.Shades.White,
                activeColor = FriendlyLinesColors.Shades.White,
                disabledColor = FriendlyLinesColors.Shades.White,
                defaultContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                activeContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                disabledContentColor = FriendlyLinesColors.Neutral.N300
            )
        },
    )
}