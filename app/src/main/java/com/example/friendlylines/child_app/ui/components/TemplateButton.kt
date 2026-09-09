package com.example.friendlylines.child_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

@Composable
fun TemplateButton(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    isDialogButton: Boolean = false,
    onClick: () -> Unit,
    textOne: String,
    textTwo: String,
    icon: ImageVector?,
    defaultColor: Color = Primary700,
    activeColor: Color = Primary900,
    disabledColor: Color = Neutral300,
    defaultContentColor: Color = Color.White,
    activeContentColor: Color = Color.White,
    disabledContentColor: Color = Color.White
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor = when {
        !enabled -> disabledColor
        isPressed -> activeColor
        else -> defaultColor
    }

    val contentColor = when {
        !enabled -> disabledContentColor
        isPressed -> activeContentColor
        else -> defaultContentColor
    }

    Box(
        modifier = modifier
            .then(
                if (!isDialogButton) {
                    Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(10.dp)
                        )
                } else {
                    Modifier
                }
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally){
                Text(
                    text = textOne,
                    color = contentColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = textTwo,
                    color = contentColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}