package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val DIALOG_SHADOW_ELEVATION = 24.dp
private val DIALOG_CONTENT_PADDING = 24.dp

@Composable
fun TemplateAlertDialog(
    title: String,
    message: String?,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    Dialog (
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .shadow(
                    elevation = DIALOG_SHADOW_ELEVATION.scaled(),
                    shape = FriendlyLinesModalShape
                ),
            shape = FriendlyLinesModalShape,
            color = FriendlyLinesColors.Shades.White
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(DIALOG_CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(16.dp.scaled())
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        modifier = Modifier.size(24.dp.scaled())
                    )

                    Spacer(modifier = Modifier.width(12.dp.scaled()))

                    Text(
                        text = title,
                        style = FriendlyLinesTextStyles.headingH5Medium,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        maxLines = 1,
                        softWrap = false
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TemplateClickableIcon(
                        icon = Icons.Filled.Close,
                        contentDescription = null,
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp.scaled())
                    )
                }

                if (message != null ){
                    Text(
                        text = message,
                        style = FriendlyLinesTextStyles.bodyRegular,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TemplateButton(
                        modifier = Modifier.wrapContentWidth(),
                        enabled = true,
                        isDialogButton = true,
                        onClick = onDismiss,
                        text = dismissText,
                        icon = null,
                        defaultColor = FriendlyLinesColors.Shades.White,
                        activeColor = FriendlyLinesColors.Shades.White,
                        disabledColor = FriendlyLinesColors.Shades.White,
                        defaultContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                        activeContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        disabledContentColor = FriendlyLinesColors.Neutral.N300
                    )

                    Spacer(modifier = Modifier.width(8.dp.scaled()))

                    TemplateButton(
                        modifier = Modifier.wrapContentWidth(),
                        enabled = true,
                        isDialogButton = true,
                        onClick = onConfirm,
                        text = confirmText,
                        icon = null,
                        defaultColor = FriendlyLinesColors.Shades.White,
                        activeColor = FriendlyLinesColors.Shades.White,
                        disabledColor = FriendlyLinesColors.Shades.White,
                        defaultContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                        activeContentColor = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        disabledContentColor = FriendlyLinesColors.Neutral.N300
                    )
                }
            }
        }
    }
}