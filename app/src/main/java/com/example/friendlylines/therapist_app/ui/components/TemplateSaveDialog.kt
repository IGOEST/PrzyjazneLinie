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
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.components.NameError

private val DIALOG_SHADOW_ELEVATION = 24.dp
private val DIALOG_CONTENT_PADDING = 24.dp

@Composable
fun TemplateSaveDialog(
    title: String,
    confirmText: String,
    dismissText: String,
    textFieldLabel: String,
    name: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit,
    nameError: NameError?
) {
    Dialog(
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
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Save,
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

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = textFieldLabel,
                            style = FriendlyLinesTextStyles.bodyRegular
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                        unfocusedBorderColor = FriendlyLinesColors.Neutral.N300,
                        focusedLabelColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                        unfocusedLabelColor = FriendlyLinesColors.Neutral.N300,
                        errorBorderColor = FriendlyLinesColors.States.Error700,
                        errorLabelColor = FriendlyLinesColors.States.Error700,
                        cursorColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                    ),
                    singleLine = true,
                    isError = nameError != null,
                    supportingText = {
                        nameError?.let { error ->
                            Text(
                                text = when (error) {
                                    NameError.BLANK -> stringResource(R.string.name_blank_error)
                                    NameError.EXISTS -> stringResource(R.string.name_exists_error)
                                },
                                color = FriendlyLinesColors.States.Error700,
                                style = FriendlyLinesTextStyles.captionC1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 4.dp.scaled()
                                    ),
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                )

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
                        enabled = name.isNotBlank(),
                        isDialogButton = true,
                        onClick = onSave,
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