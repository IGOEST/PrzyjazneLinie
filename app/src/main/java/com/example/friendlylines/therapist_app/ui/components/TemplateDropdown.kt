package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val DROPDOWN_HEIGHT = 54.dp
private val HEADER_SPACING = 4.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TemplateDropdown(
    value: T?,
    options: List<T>,
    onOptionSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    dropdownWidth: Dp? = null,
    optionLabel: (T?) -> String = { it.toString() },
    enabled: Boolean = true,
    showHeader: Boolean = false,
    showInfoIcon: Boolean = false,
    infoTitle: String? = null,
    infoMessage: String? = null,
    headerText: String? = null,
    boxIcon: ImageVector? = null,
    showEmptyOption: Boolean = false,
    emptyOptionLabel: String = "",
    headerTitleColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedText = value?.let(optionLabel) ?: label

    val headerSpacing = HEADER_SPACING.scaled()

    Column(
        modifier = modifier.wrapContentWidth()
    ) {
        if (showHeader) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showInfoIcon) {
                    InfoIconButton(
                        infoTitle = infoTitle,
                        infoMessage = infoMessage,
                        modifier = Modifier.size(24.dp.scaled()),
                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                        disabledTint = FriendlyLinesColors.Neutral.N300
                    )

                    Spacer(modifier = Modifier.width(16.dp.scaled()))
                }

                if (headerText != null) {
                    Text(
                        text = headerText,
                        color = headerTitleColor,
                        style = FriendlyLinesTextStyles.bodyRegular
                    )
                }
            }

            Spacer(modifier = Modifier.height(headerSpacing))
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .then(
                        if(dropdownWidth != null) {
                            Modifier.width(dropdownWidth.scaled())
                        } else {
                            Modifier.weight(1f)
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        if (enabled) {
                            expanded = it
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = selectedText,
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        textStyle = FriendlyLinesTextStyles.bodyRegular,
                        leadingIcon = if (boxIcon != null) {
                            {
                                Icon(
                                    imageVector = boxIcon,
                                    contentDescription = null,
                                    tint = FriendlyLinesColors.PrimaryFriendlyLines.P900
                                )
                            }
                        } else {
                            null
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                            unfocusedTextColor = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                            disabledTextColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                            focusedBorderColor = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                            unfocusedBorderColor = FriendlyLinesColors.Neutral.N300,
                            disabledBorderColor = FriendlyLinesColors.Neutral.N300,
                            focusedContainerColor = FriendlyLinesColors.Shades.White,
                            unfocusedContainerColor = FriendlyLinesColors.Shades.White,
                            disabledContainerColor = FriendlyLinesColors.Shades.White,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DROPDOWN_HEIGHT.scaled())
                            .menuAnchor(
                                ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                enabled,
                            ),
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier
                            .then(
                                if(dropdownWidth != null) {
                                    Modifier.width(dropdownWidth.scaled())
                                } else {
                                    Modifier.weight(1f)
                                }
                            )
                            .heightIn(
                                max = DROPDOWN_HEIGHT.scaled() * 4
                            )
                            .background(color = FriendlyLinesColors.Shades.White)
                    ) {
                        if (showEmptyOption) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (value == null) {
                                            FriendlyLinesColors.PrimaryFriendlyLines.P300
                                        } else {
                                            FriendlyLinesColors.Shades.White
                                        }
                                    )
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = emptyOptionLabel,
                                            style = FriendlyLinesTextStyles.bodyRegular,
                                            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                                        )
                                    },
                                    onClick = {
                                        onOptionSelected(null)
                                        expanded = false
                                    },
                                )
                            }
                        }

                        options.forEach { option ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (option == value) {
                                            FriendlyLinesColors.PrimaryFriendlyLines.P300
                                        } else {
                                            FriendlyLinesColors.Shades.White
                                        }
                                    )
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = optionLabel(option),
                                            style = FriendlyLinesTextStyles.bodyRegular,
                                            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                                        )
                                    },
                                    onClick = {
                                        onOptionSelected(option)
                                        expanded = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}