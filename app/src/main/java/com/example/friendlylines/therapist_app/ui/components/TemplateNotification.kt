package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable

@Composable
fun TemplateNotification(
    snackbarData: SnackbarData
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(
                color = Primary900,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(
                horizontal = 24.dp,
                vertical = 14.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackbarData.visuals.message,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
    }
}