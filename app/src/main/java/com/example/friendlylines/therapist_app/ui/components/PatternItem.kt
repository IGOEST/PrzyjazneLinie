package com.example.friendlylines.therapist_app.ui.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import com.example.friendlylines.therapist_app.ui.materials.models.PatternPreview
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Neutral400
import com.example.friendlylines.therapist_app.ui.theme.Primary500
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

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
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
    ) {
        Box (
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Neutral300,
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            PatternPreview(
                drawing = pattern.drawing,
                smoothingEnabled = pattern.pattern.smoothingEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )

            if (!pattern.pattern.isExample) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(bottomStart = 10.dp)
                        )
                ) {
                    TemplateClickableIcon(
                        icon = Icons.Default.Delete,
                        contentDescription = null,
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .size(24.dp)
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
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(4.dp)
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
            Icon(
                imageVector = Icons.Default.AddBox,
                contentDescription = "Dodaj własny wzór",
                tint = if (isPressed) {
                    Primary900
                } else {
                    Primary700
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.add_pattern_text),
                color = if (isPressed) {
                    Primary900
                } else {
                    Primary700
                },
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
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
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = if (selected) Primary500 else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
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
                        Neutral400
                    } else {
                        Neutral300
                    },
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            PatternPreview(
                drawing = pattern.drawing,
                smoothingEnabled = pattern.pattern.smoothingEnabled,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
        }
    }
}