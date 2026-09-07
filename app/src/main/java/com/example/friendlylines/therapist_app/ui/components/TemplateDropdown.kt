package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

@Composable
fun <T> TemplateDropdown(
    value: T?,
    options: List<T>,
    onOptionSelected: (T?) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    width: Dp = 350.dp,
    height: Dp = 54.dp,
    optionLabel: (T?) -> String = { it.toString() },
    enabled: Boolean = true,
    showHeader: Boolean = false,
    headerText: String? = null,
    headerIcon: ImageVector? = null,
    headerIconContentDescription: String? = null,
    onHeaderIconClick: (() -> Unit)? = null,
    boxIcon: ImageVector? = null,
    boxIconContentDescription: String? = null,
    showEmptyOption: Boolean = false,
    emptyOptionLabel: String = ""
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    val borderColor = if (expanded) {
        Primary700
    } else {
        Neutral300
    }

    val headerSpacing = 4.dp

    Column(
        modifier = modifier.width(width)
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
//                        start = 4.dp,
                        bottom = headerSpacing
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (headerIcon != null && onHeaderIconClick != null
                ) {
                    TemplateClickableIcon(
                        icon = headerIcon,
                        contentDescription = headerIconContentDescription,
                        onClick = onHeaderIconClick,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (headerText != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = headerText,
                        color = Primary1000,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable(
                    enabled = enabled,
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    expanded = !expanded
                }
                .padding(8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (boxIcon != null) {
                    Icon(
                        imageVector = boxIcon,
                        contentDescription = boxIconContentDescription,
                        tint = Primary900,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                }

                Text(
                    text = value?.let(optionLabel) ?: label,
                    color = if (value != null || expanded) {
                        Primary1000
                    } else {
                        Neutral300
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (expanded) {
                        Icons.Default.ArrowDropUp
                    } else {
                        Icons.Default.ArrowDropDown
                    },
                    contentDescription = null,
                    tint = Primary1000,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            DropdownOptions(
                options = options,
                selectedValue = value,
                optionLabel = optionLabel,
                onOptionSelected = {
                    onOptionSelected(it)
                    expanded = false
                },
                showEmptyOption = showEmptyOption,
                emptyOptionLabel = emptyOptionLabel,
                width = width,
                itemHeight = height
            )
        }
    }
}

@Composable
private fun <T> DropdownOptions(
    options: List<T>,
    selectedValue: T?,
    optionLabel: (T?) -> String,
    onOptionSelected: (T?) -> Unit,
    showEmptyOption: Boolean = false,
    emptyOptionLabel: String = "",
    width: Dp,
    itemHeight: Dp,
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    Box(
        modifier = Modifier
            .width(width)
            .heightIn(
                min = itemHeight,
                max = itemHeight * 3
            )
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        ScrollArea(
            state = scrollAreaState,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.weight(1f)
                ) {
                    if (showEmptyOption) {
                        item {
                            DropdownItem(
                                option = null,
                                selected = selectedValue == null,
                                optionLabel = { emptyOptionLabel },
                                onClick = {
                                    onOptionSelected(null)
                                },
                                itemHeight = itemHeight
                            )
                        }
                    }
                    items(
                        items = options,
                        key = {optionLabel(it)}
                    ) { option ->
                        val selected = option == selectedValue
                        DropdownItem(
                            option = option,
                            selected = selected,
                            optionLabel = optionLabel,
                            onClick = {
                                onOptionSelected(option)
                            },
                            itemHeight = itemHeight
                        )
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(4.dp)
                ) {
                    Thumb(
                        Modifier.background(
                            color = Primary700,
                            shape = RoundedCornerShape(2.dp)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun <T> DropdownItem(
    option: T?,
    selected: Boolean,
    optionLabel: (T?) -> String,
    onClick: () -> Unit,
    itemHeight: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight)
            .background(
                color = if (selected) {
                    Primary300
                } else {
                    Color.White
                }
            )
            .clickable(
                indication = null,
                interactionSource = remember {
                    MutableInteractionSource()
                },
                onClick = onClick
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = optionLabel(option),
            color = Primary1000,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}