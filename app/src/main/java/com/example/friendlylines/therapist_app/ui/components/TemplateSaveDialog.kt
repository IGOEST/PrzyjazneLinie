package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.NameError
import com.example.friendlylines.therapist_app.ui.theme.Error
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

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
                    elevation = 24.dp,
                    shape = RoundedCornerShape(10.dp)
                ),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Primary900,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Primary900,
                        maxLines = 1,
                        softWrap = false
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TemplateClickableIcon(
                        icon = Icons.Default.Close,
                        contentDescription = null,
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = onNameChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = textFieldLabel,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Normal,
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary700,
                        unfocusedBorderColor = Neutral300,
                        focusedLabelColor = Primary1000,
                        unfocusedLabelColor = Neutral300,
                        errorBorderColor = Error,
                        errorLabelColor = Error,
                        cursorColor = Primary1000
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
                                color = Error,
                                fontSize = 16.7.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 4.dp
                                    ),
                                textAlign = TextAlign.Left
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TemplateButton(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(54.dp),
                        enabled = true,
                        isDialogButton = true,
                        onClick = onDismiss,
                        text = dismissText,
                        icon = null,
                        defaultColor = Color.White,
                        activeColor = Color.White,
                        disabledColor = Color.White,
                        defaultContentColor = Primary700,
                        activeContentColor = Primary900,
                        disabledContentColor = Neutral300
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TemplateButton(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(54.dp),
                        enabled = name.isNotBlank(),
                        isDialogButton = true,
                        onClick = onSave,
                        text = confirmText,
                        icon = null,
                        defaultColor = Color.White,
                        activeColor = Color.White,
                        disabledColor = Color.White,
                        defaultContentColor = Primary700,
                        activeContentColor = Primary900,
                        disabledContentColor = Neutral300
                    )
                }
            }
        }
    }
}