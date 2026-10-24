package com.example.friendlylines.therapist_app.ui.materials.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesShapes
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.PatternPreview
import com.example.shared.data.models.PatternItem

private val PATTERN_ITEM_SHADOW_ELEVATION = 4.dp
private val PATTERN_ITEM_CONTENT_PADDING = 10.dp
private val PATTERN_DRAWING_PADDING = 12.dp

@Composable
fun PatternItem(
    pattern: PatternItem,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = PATTERN_ITEM_SHADOW_ELEVATION.scaled(),
                shape = FriendlyLinesModalShape
            )
            .background(
                color = FriendlyLinesColors.Shades.White,
                shape = FriendlyLinesModalShape
            )
            .padding(PATTERN_ITEM_CONTENT_PADDING.scaled())
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = FriendlyLinesColors.Neutral.N300,
                    shape = FriendlyLinesShapes.extraSmall
                )
        ) {
            PatternPreview(
                drawing = pattern.drawing,
                smoothingEnabled = pattern.pattern.smoothingEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PATTERN_DRAWING_PADDING.scaled())
            )

            if (!pattern.pattern.isExample) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp.scaled())
                        .background(
                            color = FriendlyLinesColors.Shades.White,
                            shape = RoundedCornerShape(bottomStart = 10.dp)
                        )
                ) {
                    TemplateClickableIcon(
                        icon = Icons.Filled.Delete,
                        contentDescription = null,
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .size(24.dp.scaled())
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun AddPatternItem(
    onCreateClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = PATTERN_ITEM_SHADOW_ELEVATION.scaled(),
                shape = FriendlyLinesModalShape
            )
            .background(
                color = FriendlyLinesColors.Shades.White,
                shape = FriendlyLinesModalShape
            )
            .padding(PATTERN_ITEM_CONTENT_PADDING.scaled())
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onCreateClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TemplateClickableIcon(
                icon = Icons.Filled.AddBox,
                contentDescription = null,
                onClick = onCreateClick,
                modifier = Modifier.size(38.dp.scaled())
            )

            Spacer(modifier = Modifier.height(8.dp.scaled()))

            Text(
                text = stringResource(R.string.add_pattern_text),
                color = if (isPressed) {
                    FriendlyLinesColors.PrimaryFriendlyLines.P900
                } else {
                    FriendlyLinesColors.PrimaryFriendlyLines.P700
                },
                style = FriendlyLinesTextStyles.bodyRegular,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PatternSelectionItem(
    pattern: PatternItem,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .shadow(
                elevation = PATTERN_ITEM_SHADOW_ELEVATION.scaled(),
                shape = FriendlyLinesModalShape
            )
            .background(
                color = if (selected) FriendlyLinesColors.PrimaryFriendlyLines.P500 else FriendlyLinesColors.Shades.White,
                shape = FriendlyLinesModalShape
            )
            .padding(PATTERN_ITEM_CONTENT_PADDING.scaled())
            .clickable(
                onClick = onClick,
                interactionSource = interactionSource,
                indication = null
            )
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (isPressed) {
                        FriendlyLinesColors.Neutral.N400
                    } else {
                        FriendlyLinesColors.Neutral.N300
                    },
                    shape = FriendlyLinesShapes.extraSmall
                )
        ) {
            PatternPreview(
                drawing = pattern.drawing,
                smoothingEnabled = pattern.pattern.smoothingEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(PATTERN_DRAWING_PADDING.scaled()),
                strokeWidth = 8.dp.scaled()
            )
        }
    }
}